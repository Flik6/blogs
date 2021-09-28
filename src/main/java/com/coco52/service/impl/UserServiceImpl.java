package com.coco52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.coco52.entity.*;
import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.RoleAccountMapper;
import com.coco52.mapper.UserMapper;
import com.coco52.service.UserService;
import com.coco52.util.FileUtils;
import com.coco52.util.JwtTokenUtil;
import com.coco52.util.MyUtils;
import com.coco52.util.RequestUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleAccountMapper roleAccountMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RequestUtils requestUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    /**
     * 注册账号  操作两个数据库表  user表 account表
     * user表存储用户详细信息
     * account 表存储用户账号密码
     *
     * @param registerUser 用户上传进来的账号密码
     * @return 1注册成功  0注册失败  2账号已被注册
     */
    @Transactional(rollbackFor = Exception.class)
    public RespResult registerUser(Account registerUser) {
        System.out.println(registerUser.getPassword());
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", registerUser.getUsername());
        Account account = accountMapper.selectOne(wrapper);
        if (account != null) {
            return RespResult.fail("账号已被注册,请更改您的用户名！");
        }
        registerUser.setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        int flag = accountMapper.insert(registerUser);
        MyUser myUser = new MyUser();
        myUser.setUuid(registerUser.getUuid());
        myUser.setState(0);
        myUser.setValidateCode(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        userMapper.insert(myUser);
        roleAccountMapper.insert(new RoleAccount(null, registerUser.getUuid(), 3));
        if (flag == 1) {

            return RespResult.success("注册成功！");
        } else {
            return RespResult.success("未知错误，请联系管理员！");
        }
    }

    /**
     * 登录方法
     *
     * @param loginAccount 用户登录的账号密码
     * @return LoginMsgVO实体类   三个code值   0   1  2
     * 0  用户尚未注册
     * 1  用户登录成功
     * 2  用户密码错误
     */
    @Override
    public RespResult login(Account loginAccount) {
        if (loginAccount == null || StringUtils.isEmpty(loginAccount.getUsername()) || StringUtils.isEmpty(loginAccount.getPassword())) {
            return RespResult.fail("请提交完整信息哦~");
        }
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(loginAccount.getUsername());
        } catch (UsernameNotFoundException e) {

            return new RespResult(400, e.getMessage(), null);
        }
        if (userDetails == null || !passwordEncoder.matches(loginAccount.getPassword(), userDetails.getPassword())) {
            return RespResult.fail("用户名或密码错误！");
        } else if (!userDetails.isEnabled()) {
            return RespResult.fail("账户不存在,请联系管理员！");
        } else if (!userDetails.isAccountNonLocked()) {
            return RespResult.fail("账号已被封禁,请联系管理员！");
        } else if (!userDetails.isCredentialsNonExpired()) {
            return RespResult.fail("密码凭证已失效,请联系管理员！");
        } else if (!userDetails.isAccountNonExpired()) {
            return RespResult.fail("账号已过期,请联系管理员！");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


//        更新数据库内上次登录时间
        //数据库内查出的完整user
        MyUser user = userMapper.selectUsersByUsername(loginAccount.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails, user);

        LocalDateTime localDateTime = LocalDateTime.now();

        //用于更新用户字段的实体
        MyUser updateUser = new MyUser();
        updateUser.setUpdateTime(localDateTime);
        updateUser.setLastLoginTime(localDateTime);
        userMapper.update(updateUser, new QueryWrapper<MyUser>().eq("uuid", user.getUuid()));

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("username", loginAccount.getUsername());
        tokenMap.put("uuid", user.getUuid());
        return RespResult.success("登陆成功", tokenMap);
    }


    /**
     * 登录方法
     * @param loginAccount 账号 密码
     * @return null：用户名或密码错误  MyUser：查询成功返回
     */
    @Override
    public MyUser selectByAccount(Account loginAccount) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        if (loginAccount.getUsername() != null || loginAccount.getUsername() != "") {
            accountWrapper.eq("username", loginAccount.getUsername());
        } else if (loginAccount.getPassword() != null || loginAccount.getPassword() != "") {
            accountWrapper.eq("password", loginAccount.getPassword());
        }
        Account account = accountMapper.selectOne(accountWrapper);
        if (account == null) {
            return null;
        }
        QueryWrapper<MyUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("uuid", account.getUuid());
        MyUser myUser = userMapper.selectOne(userWrapper);
        return myUser;
    }

    /**
     * 根据用户名查询Account
     * @param username
     * @return
     */
    @Override
    public Account selectByUsername(String username) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username", username);
        Account account = accountMapper.selectOne(accountWrapper);
        return account;
    }

    /**
     * 封禁用户-> 将数据库 lock 设为true
     * @param myUser
     * @return
     */
    @Override
    public RespResult banUser(MyUser myUser) {
        if (ObjectUtils.isEmpty(myUser.getUuid())) {
            return RespResult.fail("UUID为空,请检查之后重试！");
        }
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", myUser.getUuid());
//        myUserUpdateWrapper.set("is_lock", 1);
        MyUser myUser1 = new MyUser();
        myUser1.setIsLock(true);
        int update = userMapper.update(myUser1, myUserUpdateWrapper);
        return update == 1 ? RespResult.success("成功封禁用户！", myUser.getUuid()) : RespResult.fail("封禁失败,用户可能！", myUser.getUuid());
    }

    /**
     * 删除用户->伪删除 将available 设置为true 即不能使用
     *
     * @param myUser 用户实体  此参数内uuid不能为空
     * @return
     */
    @Override
    public RespResult delUser(MyUser myUser) {
        if (ObjectUtils.isEmpty(myUser.getUuid())) {
            return RespResult.fail("UUID为空,请检查之后重试！");
        }
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", myUser.getUuid());
        myUserUpdateWrapper.set("is_available", true);
        int update = userMapper.update(null, myUserUpdateWrapper);
        return update == 1 ? RespResult.success("成功删除用户！", myUser.getUuid()) : RespResult.fail("删除用户失败,用户可能不存在！", myUser.getUuid());
    }

    /**
     * 根据uuid 更新用户信息
     * @param myUser
     * @return
     */
    @Override
    public RespResult updateUser(MyUser myUser,HttpServletRequest request) {
        String allToken = request.getHeader(tokenHeader);
        String token = allToken.substring(tokenHead.length());
        String uuid = jwtTokenUtil.getUUIDFromToken(token);
        if (ObjectUtils.isEmpty(uuid)){
            return RespResult.fail(ResultCode.TOKEN_NOT_EXIST.getCode(),ResultCode.TOKEN_NOT_EXIST.getMessage(), null);
        }
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", uuid);
        int update = userMapper.update(myUser, myUserUpdateWrapper);
        return update == 1 ? RespResult.success("个人资料更新成功！", myUser.getUuid()) : RespResult.fail("更新个人资料失败,用户可能不存在！", myUser.getUuid());
    }

    @Override
    public RespResult showUser(HttpServletRequest request) {
        String uuid = requestUtils.getUUIDFromRequest(request);
        QueryWrapper<MyUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid",uuid);
        MyUser myUsers = userMapper.selectOne(wrapper);
        myUsers.setId(null);
        myUsers.setIsLock(null);
        myUsers.setValidateCode(null);
        myUsers.setUuid(null);
        myUsers.setUpdateTime(null);
        myUsers.setState(null);
        myUsers.setIsExpires(null);
        myUsers.setIsAvailable(null);
        if (ObjectUtils.isEmpty(myUsers.getAge())){
            myUsers.setAge(0);
        }
        return RespResult.success("查询成功！", myUsers);
    }

    @Override
    public RespResult uploadFile(MultipartFile[] files) {
        List<JSONObject> list = new ArrayList<>();


        for (int i = 0; i < files.length; i++) {
            //初始化对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", false);
            //获取远程的文件名
            String originalFilename = files[i].getOriginalFilename();
            jsonObject.put("fileName", originalFilename);
            String realName = null;
            try {
                //保存文件
                realName = FileUtils.saveFile(files[i]);
            } catch (FileNotFoundException e) {
                //文件为空抛出此异常
                jsonObject.put("exception", e.getMessage());
                continue;
//                return RespResult.fail(ResultCode.FILE_UPLOAD_ERROR.getCode(), ResultCode.FILE_UPLOAD_ERROR.getMessage(), null);
            } catch (FileUploadException e) {
                // 文件太大或文件写入 抛出此异常
                jsonObject.put("exception", e.getMessage());
                continue;
//                return RespResult.fail(ResultCode.FILE_UPLOAD_ERROR.getCode(), e.getMessage(), null);
            } finally {
                if (jsonObject.containsKey("exception")) {
                    list.add(jsonObject);
                } else {
                    jsonObject.put("state", true);
                    jsonObject.put("realName", realName);
                    //如果没有异常的话
                    list.add(jsonObject);
                }
            }
        }
        Integer successNum = 0;
        for (JSONObject jsonObject : list) {
            boolean state = (boolean) jsonObject.get("state");
            if (state) {
                successNum += 1;
            }
        }
        if (successNum == 0) {
            return RespResult.fail(ResultCode.FILE_UPLOAD_ERROR.getCode(), "oh,shit,一个也没上传成功", null);
        } else if (successNum > 0 && successNum < files.length) {
            return RespResult.success(ResultCode.FILE_PART_UPLOAD_SUCCESS.getCode(), ResultCode.FILE_PART_UPLOAD_SUCCESS.getMessage(), list);
        } else {
            return RespResult.success(ResultCode.File_UPLOAD_SUCCESS.getCode(), ResultCode.File_UPLOAD_SUCCESS.getMessage(), list);
        }
    }

    /**
     * 上传头像  根据Header内token获取uuid 设置头像
     *
     * @param request
     * @param avatar
     * @return
     */
    @Override
    public RespResult setAvatar(HttpServletRequest request, MultipartFile avatar) {
        String uuid = requestUtils.getUUIDFromRequest(request);
        String realName = null;
        try {
            realName = FileUtils.saveFile(avatar);
        } catch (Exception e) {
            return RespResult.fail(ResultCode.USER_AVATAR_UPLOAD_ERROR.getCode(), ResultCode.USER_AVATAR_UPLOAD_ERROR.getMessage(), null);
        }
        //   /2021/09/24/asdasdasdasd.png
        UpdateWrapper<MyUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("uuid", uuid);
        MyUser myUser = new MyUser();
        myUser.setAvatar(realName);
        int update = userMapper.update(myUser, wrapper);
        return update == 1 ?
                RespResult.success(ResultCode.USER_AVATAR_UPLOAD_SUCCESS.getCode(), ResultCode.USER_AVATAR_UPLOAD_SUCCESS.getMessage(), null) :
                RespResult.fail(ResultCode.USER_AVATAR_UPLOAD_ERROR.getCode(), ResultCode.USER_AVATAR_UPLOAD_ERROR.getMessage(), null);

    }

    @Override
    public BufferedImage getAvatar(String uuid) {
        QueryWrapper<MyUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        MyUser myUser = userMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(myUser)) {
            return null;
        }
        if (myUser.getAvatar() == null) {
            return null;
        }
//        \2021\09\24\ed729425-4afb-42f1-b561-549aef3b98aa.png
        File file =new File(FileUtils.uploadDir+myUser.getAvatar());
        if (!file.exists()){
            return null;
        }

        BufferedImage bufferedImage =null;
        try {
            bufferedImage=ImageIO.read(file);
        } catch (IOException e) {
            return null;
        }
        return bufferedImage;
    }
}
