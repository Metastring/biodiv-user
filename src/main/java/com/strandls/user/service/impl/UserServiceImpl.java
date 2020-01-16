/**
 * 
 */
package com.strandls.user.service.impl;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.strandls.user.dao.FollowDao;
import com.strandls.user.dao.SpeciesPermissionDao;
import com.strandls.user.dao.UserDao;
import com.strandls.user.dao.UserGroupMemberRoleDao;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.SpeciesPermission;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceImpl implements UserService {

	@Inject
	private UserDao userDao;

	@Inject
	private SpeciesPermissionDao speciesPermissionDao;

	@Inject
	private UserGroupMemberRoleDao userGroupMemberDao;

	@Inject
	private FollowDao followDao;

	@Override
	public User fetchUser(Long userId) {
		User user = userDao.findById(userId);
		return user;
	}

	@Override
	public UserIbp fetchUserIbp(Long userId) {
		User user = userDao.findById(userId);
		UserIbp ibp = new UserIbp(user.getId(), user.getName(), user.getProfilePic());
		return ibp;
	}

	@Override
	public User getUserByEmail(String userEmail) {
		return userDao.findByUserEmail(userEmail);
	}

	@Override
	public UserPermissions getUserPermissions(Long userId, String type, Long objectId) {
		List<SpeciesPermission> allowedTaxonList = speciesPermissionDao.findByUserId(userId);
		List<UserGroupMemberRole> userMemberRole = userGroupMemberDao.getUserGroup(userId);
		List<UserGroupMemberRole> userFeatureRole = userGroupMemberDao.findUserGroupbyUserIdRole(userId);
		Boolean following = null;
		if (type != null || objectId != null) {
			Follow follow = fetchByFollowObject(type, objectId, userId);
			following = false;
			if (follow != null)
				following = true;
		}
		UserPermissions permissions = new UserPermissions(allowedTaxonList, userMemberRole, userFeatureRole, following);
		return permissions;
	}

	@Override
	public Follow fetchByFollowId(Long id) {
		Follow follow = followDao.findById(id);
		return follow;
	}

	@Override
	public Follow fetchByFollowObject(String objectType, Long objectId, Long authorId) {
		Follow follow = followDao.findByObject(objectType, objectId, authorId);
		return follow;
	}

	@Override
	public List<Follow> fetchFollowByUser(Long authorId) {
		List<Follow> follows = followDao.findByUser(authorId);
		return follows;
	}

	@Override
	public Follow updateFollow(String objectType, Long objectId, Long userId) {
		Follow follow = followDao.findByObject(objectType, objectId, userId);
		if (follow == null) {
			follow = new Follow(null, 0L, objectId, objectType, userId, new Date());
			follow = followDao.save(follow);

		}
		return follow;
	}

	@Override
	public Follow unFollow(String type, Long objectId, Long userId) {
		Follow follow = followDao.findByObject(type, objectId, userId);
		if (follow != null) {
			follow = followDao.delete(follow);
		}
		return follow;
	}

}
