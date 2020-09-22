package com.offcn.user.service;

import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;

import java.util.List;

public interface UserService {
    public void registerUser(TMember member);

    public TMember login(String username,String password);

    public TMember findTmemberById(Integer id);

    public List<TMemberAddress> addressList(Integer memberId);
}
