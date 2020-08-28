package com.cao.oa.service;

import com.cao.oa.bean.UserInfo;
import com.cao.oa.dao.UserInfoDao;
import com.cao.oa.util.MD5;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserService {
  public static int PAGE_NUMBER = 10;
  public static int PAGE_USER_MANAGE_NUMBER = 10;
  @Autowired
  private UserInfoDao userInfoDao;

  /**
   * ��ȡĳ�����ŵ�ȫ���û������ֺ�jobId
   *
   * @param partId
   * @param groupId
   * @return
   */
  public List<Map<String, Object>> getAllUserNameAndJobIdOfGroup(int partId, int groupId) {
    return userInfoDao.getAllUserNameAndJobIdOfGroup(partId, groupId);
  }

  /**
   * ����ĳһ���û���ȫ����Ϣ���������룩
   *
   * @param info
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserInfoAllByJobId(UserInfo info) throws Exception {
    int part = getUserPartByJobId(info.getJobId());
    int group = getUserGroupByJobId(info.getJobId());
    if (part != info.getPart() || group != info.getGroup()) {
      return userInfoDao.changeUserInfoAllByJobId(info, true);
    } else {
      return userInfoDao.changeUserInfoAllByJobId(info, false);
    }
  }

  /**
   * ��ȡ�û�ȫ����Ϣ�������û�jobId
   *
   * @param jobId
   * @return
   */
  public UserInfo getUserInfoByJobId(String jobId) {
    return userInfoDao.getUserInfoByJobId(jobId);
  }

  /**
   * ��ȡȫ���û���Ϣ����ĳ�����Ż�ȫ�����š�partIdΪ0�����ȡȫ�����š�
   *
   * @param partId
   * @param page
   * @return
   */
  public List<Map<String, Object>> getUsersInfoOfPartByPage(int partId, int page) {
    if (page < 1) {
      page = 1;
    }
    int begin = (page - 1) * PAGE_USER_MANAGE_NUMBER;
    int end = PAGE_USER_MANAGE_NUMBER;
    if (partId == 0) {
      return userInfoDao.getUsersInfoOfAllByPageLimit(begin, end);
    } else {
      return userInfoDao.getUsersInfoOfPartByPageLimit(partId, begin, end);
    }
  }

  /**
   * ��ȡ��ҳ���������Ż�ȫ����partIdΪ0�����ȡȫ��
   *
   * @param partId
   * @return
   */
  public int getAllPageByPart(int partId) {
    int number = 0;
    if (partId == 0) {
      number = userInfoDao.getMemberNumbersOfAll();
    } else {
      number = userInfoDao.getMemberNumbersOfPart(partId);
    }
    return (int) Math.ceil(1.0 * number / PAGE_USER_MANAGE_NUMBER);
  }

  /**
   * �����û�������Ϣ���������š�С�顢�û���𣬸���jobId���ġ�
   *
   * @param jobId
   * @param part
   * @param group
   * @param kind
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserBaseInfoWithKind(String jobId, int part, int group, int kind,
      String post) throws Exception {
    int preGroup = getUserGroupByJobId(jobId);
    int prePart = getUserPartByJobId(jobId);
    if (preGroup != group || prePart != part) {
      //�ı�������
      return userInfoDao.changeUserBaseInfo(jobId, part, group, kind, post, true);
    } else {
      //û�ı�����
      return userInfoDao.changeUserBaseInfo(jobId, part, group, kind, post, false);
    }
  }

  /**
   * �����û�������Ϣ���������ź�С�飬����jobId���ġ�
   *
   * @param jobId
   * @param part
   * @param group
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserBaseInfoWithoutKind(String jobId, int part, int group, String post)
      throws Exception {
    int kind = getUserKindByJobId(jobId);
    int preGroup = getUserGroupByJobId(jobId);
    int prePart = getUserPartByJobId(jobId);
    if (preGroup != group || prePart != part) {
      //�ı�������
      return userInfoDao.changeUserBaseInfo(jobId, part, group, kind, post, true);
    } else {
      //û�ı�����
      return userInfoDao.changeUserBaseInfo(jobId, part, group, kind, post, false);
    }
  }

  /**
   * ��ȡ�û�С�顣ʧ���򷵻�-1
   *
   * @param jobId
   * @return
   */
  public int getUserGroupByJobId(String jobId) {
    return userInfoDao.getUserGroupByJobId(jobId);
  }

  /**
   * ��ȡ�û����š�ʧ���򷵻�-1
   *
   * @param jobId
   * @return
   */
  public int getUserPartByJobId(String jobId) {
    return userInfoDao.getUserPartByJobId(jobId);
  }

  /**
   * ��ȡ�û���
   *
   * @param jobId
   * @return
   */
  public String getUserNameById(String jobId) {
    return userInfoDao.getUserNameByJobId(jobId);
  }

  /**
   * �����������û�
   *
   * @param usersList
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
  public boolean createNewUsers(List<UserInfo> usersList) throws Exception {
    boolean res = false;
    //������
    if (usersList != null && usersList.size() != 0) {
      for (int i = 0; i < usersList.size(); i++) {
        UserInfo user = usersList.get(i);
        //				System.out.println("Service:("+i+")"+user.getKind());
        if (user.getJobId() != null && user.getCardId() != null && user.getName() != null
            && user.getSex() != -1 && user.getPart() != -1 && user.getGroup() != -1) {
          if (user.getKind() == 0) {
            usersList.get(i).setKind(UserInfo.KIND_MEMBER);//���û�У���ΪĬ��ֵ
          }
          if (user.getPassword() == null) {
            usersList.get(i).setPassword(md5PasswordFirst("123456"));//���û�У���ΪĬ��ֵ
          }
        } else {
          //ȱ�ٶ��������������
          //					System.out.println("ȱ�ٶ��������������");
          return res;
        }
      }
      //�������
      return userInfoDao.createNewUsers(usersList);
    }
    return res;
  }

  /**
   * �Ƿ�������û�
   *
   * @param jobId
   * @return
   */
  public boolean hasUserByJobId(String jobId) {
    return userInfoDao.hasUserByJobId(jobId);
  }

  /**
   * ͨ��jobId��ȡ�û�����
   *
   * @param jobId
   * @return ʧ��Ϊ-1
   */
  public int getUserKindByJobId(String jobId) {
    return userInfoDao.getUserKindByJobId(jobId);
  }

  /**
   * ��ȡ��ҳ��,��С��
   *
   * @return
   */
  public int getAllPageByGroup(int partId, int groupId) {
    int number = userInfoDao.getMemberNumbersOfGroup(partId, groupId);
    return (int) Math.ceil(1.0 * number / PAGE_NUMBER);
  }

  /**
   * ��ĳһ�������г��������������Ϣ
   *
   * @param groupId
   * @return
   */
  public List<UserInfo> findUsersGroupOfGroupId(int partId, int groupId, int page) {
    if (page < 1) {
      page = 1;
    }
    int begin = (page - 1) * PAGE_NUMBER;
    int end = PAGE_NUMBER;
    return userInfoDao.findUsersGroupOfGroupId(partId, groupId, begin, end);
  }

  /**
   * ͨ��jobId��ȡ���������������Ϣ
   *
   * @param jobId
   * @return
   */
  public Map<String, Object> getPersonInfoAllByJobId(String jobId) {
    return userInfoDao.findUserByJobId(jobId);
  }

  /**
   * ͨ��jobId�����û��Լ������䡢�绰����ַ
   *
   * @param jobId
   * @param tel
   * @param email
   * @param addr
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeMyPersonInfoByJobId(String jobId, String tel, String email, String addr)
      throws Exception {
    if (tel == null || tel.length() == 0) {
      tel = null;
    }
    if (email == null || email.length() == 0) {
      email = null;
    }
    if (addr == null || addr.length() == 0) {
      addr = null;
    }
    return userInfoDao.changeMyPersonInfoByJobId(jobId, tel, email, addr);
  }

  /**
   * ����jobId�ҵ�������
   *
   * @param jobId
   * @return
   */
  public Map<String, Object> getOtherInfoByJobId(String jobId) {
    return userInfoDao.getOtherInfoByJobId(jobId);
  }

  /**
   * ��֤��¼
   *
   * @param jobId
   * @param password
   * @return
   */
  public UserInfo checkLogin(String jobId, String password) {
    UserInfo res = null;
    password = md5Password(password);
    res = userInfoDao.checkLoginByJobId(jobId, password);
    //		if(res==null){
    //			res  = userInfoDao.checkLoginByCardId(jobId, password);
    //		}
    return res;
  }

  /**
   * �޸��Լ�������
   *
   * @param jobId
   * @param oldPassword
   * @param newPassword
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeMyPassword(String jobId, String oldPassword, String newPassword)
      throws Exception {
    boolean res = false;
    oldPassword = md5Password(oldPassword);
    newPassword = md5Password(newPassword);
    //���������ȷ
    if (userInfoDao.validatePasswordByJobId(jobId, oldPassword)) {
      //�޸�����
      if (userInfoDao.changePassword(jobId, newPassword)) {
        res = true;
      }
    }
    return res;
  }

  /**
   * ��������
   *
   * @param jobId
   * @param cardId
   * @param name
   * @param newPassword
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public Boolean forgetPassword(String jobId, String cardId, String name, String newPassword)
      throws Exception {
    Boolean res = false;
    //���ܴ���
    newPassword = md5Password(newPassword);
    //��֤�û�
    Map<String, Object> verif = userInfoDao.findUserByJobId(jobId);
    if (verif == null) {
      return res;
    }
    if (verif.get("jobId").equals(jobId) && verif.get("cardId").equals(cardId) && verif.get("name")
        .equals(name)) {
      //��֤ͨ�����Ǳ���,�޸�����
      if (userInfoDao.getUserStatusByJobId(jobId) == UserInfo.STATUS_NO_ACTIVITY) {
        //û�м�����м���
        userInfoDao.changeUserStatusByJobId(jobId, UserInfo.STATUS_NORMAL);
      }
      return userInfoDao.changePassword(jobId, newPassword);
    } else {
      return res;
    }
  }

  /**
   * �����û�״̬
   *
   * @param jobId
   * @param status
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserStatusByJobId(String jobId, int status) throws Exception {
    return userInfoDao.changeUserStatusByJobId(jobId, status);
  }

  /**
   * �ⶳ
   *
   * @param jobId
   * @return
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserStatusOutOfFrozenByJobId(String jobId) throws Exception {
    return userInfoDao.changeUserStatusOutOfFrozenByJobId(jobId);
  }

  /**
   * ����Ҫ�ⶳ���û�ô
   *
   * @return
   */
  public String hasNeedToOutOfFrozen() {
    return userInfoDao.hasNeedToOutOfFrozen();
  }

  /**
   * �������������
   *
   * @param str
   * @return
   */
  private String md5Password(String str) {
    String res = null;
    String doStr = "ruan" + str + "jian" + str;
    res = MD5.md5(doStr);
    //		System.out.println("MD5-->"+res);
    return res;
  }

  /**
   * ������һ������
   *
   * @param str
   * @return
   */
  private String md5PasswordFirst(String str) {
    String res = null;
    String doStr = str + "15180600101" + str;
    res = MD5.md5(doStr);
    return res;
  }

  /**
   * ��ѯ�û�״̬
   *
   * @param jobId
   * @return
   */
  public int getUserStatusByJobId(String jobId) {
    return userInfoDao.getUserStatusByJobId(jobId);
  }

  public UserInfoDao getUserInfoDao() {
    return userInfoDao;
  }

  public void setUserInfoDao(UserInfoDao userInfoDao) {
    this.userInfoDao = userInfoDao;
  }

  /**
   * �ı�������
   *
   * @param times
   * @throws Exception
   */
  @Transactional(readOnly = false)
  public boolean changeUserPasswordErrorTimes(String jobId, int times) throws Exception {
    return userInfoDao.changeUserPasswordErrorTimes(jobId, times);
  }

  /**
   * ��ô������
   *
   * @return
   */
  public int getUserPasawordErrorTimes(String jobId) {
    return userInfoDao.getUserPasawordErrorTimes(jobId);
  }
}
