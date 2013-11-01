/*
 * TopStack (c) Copyright 2012-2013 Transcend Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amazonaws.services.elasticache.model.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.IntNode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.amazonaws.services.elasticache.model.DescribeCacheParametersResult;
import com.generationjava.io.xml.XMLNode;
import com.google.common.base.Strings;
import com.msi.tough.cf.AccountType;
import com.msi.tough.cf.ec2.AuthorizeSecurityGroupIngressType;
import com.msi.tough.cf.ec2.SecurityGroupType;
import com.msi.tough.core.CommaObject;
import com.msi.tough.core.ExecutorHelper;
import com.msi.tough.core.ExecutorHelper.Executable;
import com.msi.tough.core.JsonUtil;
import com.msi.tough.core.StringHelper;
import com.msi.tough.engine.aws.ec2.Instance;
import com.msi.tough.engine.core.CallStruct;
import com.msi.tough.engine.core.TemplateContext;
import com.msi.tough.model.AccountBean;
import com.msi.tough.model.elasticache.CacheClusterBean;
import com.msi.tough.model.elasticache.CacheNodeBean;
import com.msi.tough.model.elasticache.CacheNodeTypeBean;
import com.msi.tough.model.elasticache.CacheNodeTypeSpecificValueBean;
import com.msi.tough.model.elasticache.CacheParameterGroupBean;
import com.msi.tough.model.elasticache.CacheParameterGroupFamilyBean;
import com.msi.tough.model.elasticache.CacheSecurityGroupBean;
import com.msi.tough.model.elasticache.ParameterBean;
import com.msi.tough.query.QueryFaults;
import com.msi.tough.utils.AccountUtil;
import com.msi.tough.utils.CFUtil;
import com.msi.tough.utils.ChefUtil;
import com.msi.tough.utils.EcacheUtil;
import com.msi.tough.utils.SecurityGroupUtils;
import com.transcend.elasticache.message.DescribeCacheParametersActionMessage.DescribeCacheParametersActionResultMessage;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheCluster;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNode;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificParameter;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheNodeTypeSpecificValue;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheParameterGroupStatus;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.CacheSecurityGroupMembership;
import com.transcend.elasticache.message.ElastiCacheMessage.EC2SecurityGroup;
import com.transcend.elasticache.message.ElastiCacheMessage.Endpoint;
import com.transcend.elasticache.message.ElastiCacheMessage.Parameter;
import com.transcend.elasticache.message.ElastiCacheMessage.ParameterNameValue;
import com.transcend.elasticache.message.ElastiCacheMessage.PendingModifiedValues;

public class EcacheUtilV2 {
    public static final String DEFAULT_GROUP = "default";

    @SuppressWarnings("unchecked")
    public static void copyParameterGroup(final Session session,
            final long sourceGroupId, final long destGroupId) {

        final Query q = session.createQuery("from ParameterBean where groupId="
                + sourceGroupId);
        final List<ParameterBean> l = q.list();

        for (final ParameterBean p : l) {
            final ParameterBean pb = new ParameterBean();
            pb.setGroupId(destGroupId);
            pb.setAllowedValues(p.getAllowedValues());
            pb.setDataType(p.getDataType());
            pb.setDescription(p.getDescription());
            pb.setMinimumEngineVersion(p.getMinimumEngineVersion());
            pb.setName(p.getName());
            pb.setParameterValue(p.getParameterValue());
            pb.setSource(p.getSource());
            pb.setModifiable(p.isModifiable());
            pb.setNodeSpecific(p.isNodeSpecific());
            session.save(pb);
        }
    }

    public static CacheParameterGroupBean createParameterGroup(
            final Session session, final long acid, final String familyName,
            final String groupName, final String description) {
        CacheParameterGroupBean b = getCacheParameterGroupBean(session, acid,
                groupName);
        if (b != null) {
            return b;
        }
        final CacheParameterGroupFamilyBean family = getParameterGroupFamily(
                session, familyName);
        if (family == null) {
            throw QueryFaults.InvalidParameterValue();
        }

        b = new CacheParameterGroupBean();
        b.setAcid(acid);
        b.setDescription(description);
        b.setFamilyId(family.getId());
        b.setName(groupName);
        session.save(b);

        final CacheParameterGroupBean familyParameterGroup = getFamilyParameterGroup(
                session, familyName);

        copyParameterGroup(session, familyParameterGroup.getId(), b.getId());
        return b;
    }

    public static CacheSecurityGroupBean createSecurityGroup(
            final Session session, final long acid, final String groupName,
            final String description, String stackId, final String parentId)
            throws Exception {
        CacheSecurityGroupBean b = getCacheSecurityGroupBean(session, acid,
                groupName);
        if (b != null) {
            return b;
        }

        if (stackId == null) {
            stackId = "__ecache_" + StringHelper.randomStringFromTime();
        }
        final String secGrpName = getSecurityGroupName(acid, groupName);

        final AccountBean account = AccountUtil.readAccount(session, acid);

        final String pid = SecurityGroupUtils.createSecurityGroup(
                AccountUtil.toAccount(account), new TemplateContext(null),
                parentId, stackId, account.getDefZone(), secGrpName,
                description);

        b = new CacheSecurityGroupBean();
        b.setAcid(acid);
        b.setDescription(description);
        b.setName(groupName);
        b.setStackId(stackId);
        b.setProviderId(pid);
        b.setProviderName(secGrpName);
        session.save(b);

        return b;
    }

    public static void deleteParameterGroupBean(final Session session,
            final long acid, final String name) {
        final CacheParameterGroupBean b = getCacheParameterGroupBean(session,
                acid, name);
        final List<ParameterBean> ps = selectParameterBean(session, b.getId());
        for (final ParameterBean p : ps) {
            session.delete(p);
        }
        session.delete(b);
    }

    public static void deleteSecurityGroupBean(final Session session,
            final AccountType ac, final String name) throws Exception {
        final CacheSecurityGroupBean b = getCacheSecurityGroupBean(session,
                ac.getId(), name);
        CFUtil.deleteStackResources(ac, b.getStackId(), name, null);
        session.delete(b);
    }

    @SuppressWarnings("unchecked")
    public static void ensureDefaultParameterGroup(final Session session,
            final long acid) {
        final Query q = session
                .createQuery("from CacheParameterGroupFamilyBean");
        final List<CacheParameterGroupFamilyBean> l = q.list();
        if (l != null) {
            for (final CacheParameterGroupFamilyBean b : l) {
                if (getCacheParameterGroupBean(session, acid, DEFAULT_GROUP) != null) {
                    return;
                }
                createParameterGroup(session, acid, b.getFamily(),
                        DEFAULT_GROUP, "Defaut Parameter Group");
            }
        }
    }

    public static void ensureDefaultSecurityGroup(final Session session,
            final long acid) throws Exception {
        if (getCacheSecurityGroupBean(session, acid, DEFAULT_GROUP) != null) {
            return;
        }
        final CacheSecurityGroupBean grp = createSecurityGroup(session, acid,
                DEFAULT_GROUP, "Defaut Elasticache Security Group", null, null);
        final AccountBean account = AccountUtil.readAccount(session, acid);
        // final String secGrpName = getSecurityGroupName(acid, DEFAULT_GROUP);
        SecurityGroupUtils.authorizeSecurityGroupIngress(
                AccountUtil.toAccount(account), grp.getProviderId(), 11211,
                grp.getStackId());
    }

    public static CacheClusterBean getCacheClusterBean(final Session session,
            final long acid, final String name) {
        final List<CacheClusterBean> l = selectCacheClusterBean(session, acid,
                name);
        if (l == null || l.size() == 0) {
            return null;
        }
        return l.get(0);
    }

    @SuppressWarnings("unchecked")
    public static CacheNodeTypeBean getCacheNodeTypeBean(final Session session,
            final long id) {
        final Query q = session.createQuery("from CacheNodeTypeBean where id="
                + id);
        final List<CacheNodeTypeBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheNodeTypeBean getCacheNodeTypeBean(final Session session,
            final String type) {
        final Query q = session
                .createQuery("from CacheNodeTypeBean where type='" + type + "'");
        final List<CacheNodeTypeBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheNodeTypeSpecificValueBean getCacheNodeTypeSpecificValueBean(
            final Session session, final long parameterId, final long nodeTypeId) {
        final Query q = session
                .createQuery("from CacheNodeTypeSpecificValueBean where parameterId="
                        + parameterId + " and nodeTypeId=" + nodeTypeId);
        final List<CacheNodeTypeSpecificValueBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheParameterGroupBean getCacheParameterGroupBean(
            final Session session, final long acid, final String name) {
        final Query q = session
                .createQuery("from CacheParameterGroupBean where acid=" + acid
                        + " and name='" + name + "'");
        final List<CacheParameterGroupBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheSecurityGroupBean getCacheSecurityGroupBean(
            final Session session, final long acid, final String name) {
        final Query q = session
                .createQuery("from CacheSecurityGroupBean where acid=" + acid
                        + " and name='" + name + "'");
        final List<CacheSecurityGroupBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    public static String getDatabagName(final long acid, final String name) {
        return "__ecache-" + acid + "-" + name;
    }

    public static CacheParameterGroupBean getFamilyParameterGroup(
            final Session session, final String familyName) {
        return getCacheParameterGroupBean(session, 0, DEFAULT_GROUP + "."
                + familyName);
    }

    @SuppressWarnings("unchecked")
    public static CacheParameterGroupFamilyBean getParameterGroupFamily(
            final Session session, final int id) {
        final Query q = session
                .createQuery("from CacheParameterGroupFamilyBean where id="
                        + id);
        final List<CacheParameterGroupFamilyBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheParameterGroupFamilyBean getParameterGroupFamily(
            final Session session, final long familyId) {
        final Query q = session
                .createQuery("from CacheParameterGroupFamilyBean where id="
                        + familyId);
        final List<CacheParameterGroupFamilyBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CacheParameterGroupFamilyBean getParameterGroupFamily(
            final Session session, final String familyName) {
        final Query q = session
                .createQuery("from CacheParameterGroupFamilyBean where family='"
                        + familyName + "'");
        final List<CacheParameterGroupFamilyBean> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    public static XMLNode getRootNode(final String tag) {
        final XMLNode nodeRoot = new XMLNode(tag);
        nodeRoot.addAttr("xmlns",
                "http://elasticache.amazonaws.com/doc/2012-03-09/");
        return nodeRoot;
    }

    public static String getSecurityGroupName(final long acid,
            final String groupName) {
        return "__ecache_" + acid + "_" + groupName;
    }

    public static String getStackName(final long acid, final String name) {
        return String.format("__ecache-%d-%s", acid, name);
    }

    public static void rebootCluster(final Session session,
            final CacheClusterBean cc, final AccountType account) {

        cc.setCacheClusterStatus("pending-reboot");
        if (cc.getParameterGroupStatus() == null
                || !cc.getParameterGroupStatus().equals("in-sync")) {
            cc.setParameterGroupStatus("pending-reboot");
        } else {
            cc.setParameterGroupStatus("in-sync");
        }
        session.save(cc);
        final List<CacheNodeBean> lnb = EcacheUtil.selectCacheNodeBean(session,
                cc.getId());
        final List<String> instances = new ArrayList<String>();
        for (final CacheNodeBean nb : lnb) {
            nb.setNodeStatus("pending-reboot");
            nb.setParameterGroupStatus(cc.getParameterGroupStatus());
            session.save(nb);
            instances.add(nb.getInstaceId());
        }

        final String databag = getDatabagName(account.getId(), cc.getName());
        try {
            final String bag = ChefUtil.getDatabagItem(databag, "config");
            final JsonNode o = JsonUtil.load(bag);
            final Map<String, Object> m = JsonUtil.toMap(o);
            String revision = "0";
            if (m.containsKey("revision")) {
                final Object obj = m.get("revision");
                if (obj instanceof IntNode) {
                    revision = ((IntNode) obj).getValueAsText();
                }
                if (obj instanceof String) {
                    revision = (String) obj;
                }
            }
            revision = String.valueOf(Integer.parseInt(revision) + 1);
            m.put("revision", revision);
            final String json = JsonUtil.toJsonString(m);
            ChefUtil.putDatabagItem(databag, "config", json);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final Executable r = new ExecutorHelper.Executable() {
            @Override
            public void run() {
                try {
                    rebootInstance(account, instances);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ExecutorHelper.execute(r);
    }

    private static void rebootInstance(final AccountType account,
            final List<String> instances) throws Exception {
        for (final String c : instances) {
            final CallStruct call = new CallStruct();
            call.setCtx(new TemplateContext(null));
            call.setAc(account);
            call.setPhysicalId(c);
            final Instance prov = new Instance();
            prov.reboot(call);
        }
    }

    @SuppressWarnings("unchecked")
    public static void resetParameters(final Session session,
            final AccountType ac, final String name,
            final List<ParameterNameValue> vals,
            final Boolean resetAllParameters) {

        final CacheParameterGroupBean pgrp = getCacheParameterGroupBean(
                session, ac.getId(), name);
        final CacheParameterGroupFamilyBean fb = getParameterGroupFamily(
                session, pgrp.getFamilyId());
        final CacheParameterGroupBean fgrp = getFamilyParameterGroup(session,
                fb.getFamily());
        final Query fq = session
                .createQuery("from ParameterBean where groupId=" + fgrp.getId());
        final List<ParameterBean> fl = fq.list();

        final Query q = session.createQuery("from ParameterBean where groupId="
                + pgrp.getId());
        final List<ParameterBean> l = q.list();

        for (final ParameterBean pb : l) {
            boolean reset = false;
            if (resetAllParameters) {
                reset = true;
            } else {
                for (final ParameterNameValue v : vals) {
                    if (v.getParameterName().equals(pb.getName())) {
                        reset = true;
                        break;
                    }
                }
            }
            if (reset) {
                for (final ParameterBean f : fl) {
                    if (f.getName().equals(pb.getName())) {
                        pb.setParameterValue(f.getParameterValue());
                        pb.setSource(f.getSource());
                        session.save(pb);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List<CacheClusterBean> selectCacheClusterBean(
            final Session session, final long acid, final String name) {
        final Query q = session.createQuery("from CacheClusterBean where acid="
                + acid + (name != null ? " and name='" + name + "'" : ""));
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public static List<CacheNodeBean> selectCacheNodeBean(
            final Session session, final long clusterId) {
        final Query q = session
                .createQuery("from CacheNodeBean where cacheCluster="
                        + clusterId);
        final List<CacheNodeBean> l = q.list();
        return l;
    }

    @SuppressWarnings("unchecked")
    public static List<ParameterBean> selectParameterBean(
            final Session session, final long grpid) {
        final Query q = session.createQuery("from ParameterBean where groupId="
                + grpid);
        final List<ParameterBean> l = q.list();
        return l;
    }

    @SuppressWarnings("unchecked")
    public static CacheCluster toAwsCacheCluster(final Session session,
            final CacheClusterBean b) {

        // Convert to AMAZON object for DTO
        final CacheCluster.Builder awsCacheCluster = CacheCluster.newBuilder();
        awsCacheCluster.setAutoMinorVersionUpgrade(b
                .getAutoMinorVersionUpgrade());
        awsCacheCluster.setCacheClusterCreateTime(b.getCreatedTime().toString());
        awsCacheCluster.setCacheClusterId(b.getName());
        awsCacheCluster.setCacheClusterStatus(b.getCacheClusterStatus());
        final CacheNodeTypeBean nodeType = getCacheNodeTypeBean(session,
                b.getNodeTypeId());
        if (nodeType != null) {
            awsCacheCluster.setCacheNodeType(nodeType.getType());
        }

        // cache nodes
        {
            final Collection<CacheNode> awsCacheNodes = new ArrayList<CacheNode>();
            final List<CacheNodeBean> l = selectCacheNodeBean(session,
                    b.getId());
            int seq = 0;
            for (final CacheNodeBean n : l) {
                seq++;
                final CacheNode.Builder awsCacheNode = CacheNode.newBuilder();
                awsCacheNode.setCacheNodeCreateTime(n.getCreatedTime().toString());
                awsCacheNode.setCacheNodeStatus(n.getNodeStatus());
                awsCacheNode.setCacheNodeId(Integer.toString(seq));
                final Endpoint.Builder endpoint = Endpoint.newBuilder();
                endpoint.setAddress(n.getAddress());
                endpoint.setPort(b.getPort());
                awsCacheNode.setEndpoint(endpoint);
                awsCacheNodes.add(awsCacheNode.buildPartial());
                awsCacheNode.setParameterGroupStatus(n
                        .getParameterGroupStatus());
            }
            awsCacheCluster.addAllCacheNodes(awsCacheNodes);
        }

        // Parameter Group
        {
            final Query q = session
                    .createQuery("from CacheParameterGroupBean where id="
                            + b.getParameterGroupId());
            final List<CacheParameterGroupBean> l = q.list();
            final CacheParameterGroupBean pgb = l.get(0);
            final CacheParameterGroupStatus.Builder cacheParameterGroup = CacheParameterGroupStatus.newBuilder();
            cacheParameterGroup.setCacheParameterGroupName(pgb.getName());
            cacheParameterGroup.setParameterApplyStatus(Strings.nullToEmpty(b
                    .getParameterGroupStatus()));
            awsCacheCluster.setCacheParameterGroup(cacheParameterGroup);
        }

        // Security Group
        {
            final CommaObject sgs = new CommaObject(b.getSecurityGroups());
            final Collection<CacheSecurityGroupMembership> awsCacheSecurityGroups = new ArrayList<CacheSecurityGroupMembership>();
            for (final String sg : sgs.toList()) {
                final CacheSecurityGroupMembership.Builder awsSecurityGroup = CacheSecurityGroupMembership.newBuilder();
                awsSecurityGroup.setCacheSecurityGroupName(sg);
                awsCacheSecurityGroups.add(awsSecurityGroup.buildPartial());
            }
            awsCacheCluster.addAllCacheSecurityGroups(awsCacheSecurityGroups);
        }
        // // Notification Configuration
        // final NotificationConfiguration nf = new NotificationConfiguration();
        // nf.setTopicArn(getNotificationTopicArn());
        // nf.setTopicStatus(getNotificationTopicStatus().name());
        // awsCacheCluster.setNotificationConfiguration(nf);

        awsCacheCluster.setNumCacheNodes(b.getNodeCount());

        // Pending Modified Values
        final PendingModifiedValues.Builder pending = PendingModifiedValues.newBuilder();
        if (b.getNewEngineVersion() != null
                && !b.getNewEngineVersion().equals(b.getEngineVersion())) {
            pending.setEngineVersion(b.getNewEngineVersion());
        }
        if (b.getNewNodeCount() != null && b.getNodeCount() != null
                && b.getNodeCount() != b.getNewNodeCount()) {
            pending.setNumCacheNodes(b.getNewNodeCount());
            if (b.getNewNodeCount() < b.getNodeCount()) {
                final Collection<String> remove = new ArrayList<String>();
                final List<CacheNodeBean> nodes = EcacheUtil
                        .selectCacheNodeBean(session, b.getId());
                int i = 0;
                for (final CacheNodeBean node : nodes) {
                    i++;
                    if (node.getNodeStatus().equals("removing")) {
                        remove.add("" + i);
                    }
                }
                pending.addAllCacheNodeIdsToRemove(remove);
            }
        }
        awsCacheCluster.setPendingModifiedValues(pending);

        awsCacheCluster.setPreferredAvailabilityZone(Strings.nullToEmpty(b
                .getPreferredAvailabilityZone()));

        awsCacheCluster.setPreferredMaintenanceWindow(Strings.nullToEmpty(b
                .getPreferredMaintenanceWindow()));

        // engine
        awsCacheCluster.setEngine(b.getEngine());
        awsCacheCluster.setEngineVersion(b.getEngineVersion());

        return awsCacheCluster.buildPartial();
    }

    public static CacheParameterGroup toAwsCacheParameterGroup(
            final Session session, final CacheParameterGroupBean b) {
        final CacheParameterGroup.Builder awsCacheParameterGroup = CacheParameterGroup.newBuilder();
        final CacheParameterGroupFamilyBean family = getParameterGroupFamily(
                session, b.getFamilyId());
        if (family != null) {
            awsCacheParameterGroup.setCacheParameterGroupFamily(family
                    .getFamily());
        }
        awsCacheParameterGroup.setCacheParameterGroupName(b.getName());
        awsCacheParameterGroup.setDescription(b.getDescription());

        return awsCacheParameterGroup.buildPartial();
    }

    public static CacheSecurityGroup toAwsCacheSecurityGroup(
            final Session session, final CacheSecurityGroupBean b)
            throws Exception {
        final CacheSecurityGroup.Builder grp = CacheSecurityGroup.newBuilder();
        grp.setCacheSecurityGroupName(b.getName());
        grp.setDescription(b.getDescription());
        final AccountBean ac = AccountUtil.readAccount(session, b.getAcid());
        grp.setOwnerId(ac.getId() + "");

        final SecurityGroupType st = SecurityGroupUtils.describeSecurityGroup(
                AccountUtil.toAccount(ac), ac.getDefZone(), b.getProviderId());
        if (st.getSecurityGroupIngress() != null) {
            final Collection<EC2SecurityGroup> grps = new ArrayList<EC2SecurityGroup>();
            for (final AuthorizeSecurityGroupIngressType t : st
                    .getSecurityGroupIngress()) {
                if (t.getSourceSecurityGroupName() != null) {
                    final EC2SecurityGroup.Builder g = EC2SecurityGroup.newBuilder();
                    g.setEc2SecurityGroupName(t.getSourceSecurityGroupName());
                    g.setEc2SecurityGroupOwnerId(ac.getTenant() + "");
                    g.setStatus("authorized");
                    grps.add(g.buildPartial());
                }
            }
            grp.addAllEc2SecurityGroups(grps);
        }
        return grp.buildPartial();
    }

    public static Parameter toAwsParameter(final ParameterBean b) {
        final Parameter.Builder awsParameter = Parameter.newBuilder();
        awsParameter.setAllowedValues(b.getAllowedValues());
        awsParameter.setDataType(b.getDataType());
        awsParameter.setDescription(b.getDescription());
        awsParameter.setIsModifiable(b.isModifiable());
        awsParameter.setMinimumEngineVersion("" + b.getMinimumEngineVersion());
        awsParameter.setParameterName(b.getName());
        awsParameter.setParameterValue(b.getParameterValue());
        awsParameter.setSource(b.getSource());
        return awsParameter.buildPartial();
    }

    @SuppressWarnings("unchecked")
    public static DescribeCacheParametersActionResultMessage.Builder toDescribeCacheParametersResult(
            final Session session, final long grpId, final String source) {
        final DescribeCacheParametersActionResultMessage.Builder ret = DescribeCacheParametersActionResultMessage.newBuilder();
        final List<ParameterBean> l = EcacheUtil.selectParameterBean(session,
                grpId);
        final Collection<CacheNodeTypeSpecificParameter> specificParameters = new ArrayList<CacheNodeTypeSpecificParameter>();
        final Collection<Parameter> parameters = new ArrayList<Parameter>();
        for (final ParameterBean b : l) {
            if (source != null && b.getSource() != null
                    && !source.equals(b.getSource())) {
                continue;
            }
            if (b.isNodeSpecific()) {
                final CacheNodeTypeSpecificParameter.Builder p = CacheNodeTypeSpecificParameter.newBuilder();
                p.setAllowedValues(b.getAllowedValues());
                final Collection<CacheNodeTypeSpecificValue> cacheNodeTypeSpecificValues = new ArrayList<CacheNodeTypeSpecificValue>();
                final Query qtype = session
                        .createQuery("from CacheNodeTypeBean");
                final List<CacheNodeTypeBean> ltype = qtype.list();
                for (final CacheNodeTypeBean type : ltype) {
                    final CacheNodeTypeSpecificValueBean vb = EcacheUtil
                            .getCacheNodeTypeSpecificValueBean(session,
                                    b.getId(), type.getId());
                    if (vb != null) {
                        final CacheNodeTypeSpecificValue.Builder v = CacheNodeTypeSpecificValue.newBuilder();
                        v.setCacheNodeType(type.getType());
                        v.setValue(vb.getParameterValue());
                        cacheNodeTypeSpecificValues.add(v.buildPartial());
                    }
                }
                p.addAllCacheNodeTypeSpecificValues(cacheNodeTypeSpecificValues);
                p.setDataType(b.getDataType());
                p.setDescription(b.getDescription());
                p.setIsModifiable(b.isModifiable());
                p.setMinimumEngineVersion("" + b.getMinimumEngineVersion());
                p.setParameterName(b.getName());
                p.setSource(b.getSource());
                specificParameters.add(p.buildPartial());
            } else {
                final Parameter.Builder p = Parameter.newBuilder();
                p.setAllowedValues(b.getAllowedValues());
                p.setParameterValue(b.getParameterValue());
                p.setDataType(b.getDataType());
                p.setDescription(b.getDescription());
                p.setIsModifiable(b.isModifiable());
                p.setMinimumEngineVersion("" + b.getMinimumEngineVersion());
                p.setParameterName(b.getName());
                p.setSource(b.getSource());
                parameters.add(p.buildPartial());
            }
        }
        ret.addAllCacheNodeTypeSpecificParameters(specificParameters);
        ret.addAllParameters(parameters);
        return ret;
    }

    public static boolean updateParameter(final Session session,
            final CacheParameterGroupBean parameterGroup,
            final ParameterNameValue nv) {

        return true;
    }

    @SuppressWarnings("unchecked")
    public static void updateParameters(final Session session,
            final long sourceGroupId, final List<ParameterNameValue> vals) {

        final Query q = session.createQuery("from ParameterBean where groupId="
                + sourceGroupId);
        final List<ParameterBean> l = q.list();

        for (final ParameterBean pb : l) {
            for (final ParameterNameValue v : vals) {
                if (v.getParameterName().equals(pb.getName())) {
                    if (v.getParameterValue() == null) {
                        throw QueryFaults.InvalidParameterValue();
                    }
                    if (!pb.isModifiable()) {
                        throw QueryFaults.InvalidParameterCombination(pb
                                .getName() + " is a non-modifiable parameter");
                    }
                    pb.setParameterValue(v.getParameterValue());
                    pb.setSource("user");
                    session.save(pb);
                    break;
                }
            }
        }
    }
}
