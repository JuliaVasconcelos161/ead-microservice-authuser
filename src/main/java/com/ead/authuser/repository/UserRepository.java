package com.ead.authuser.repository;

import com.ead.authuser.model.UserModel;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
}
