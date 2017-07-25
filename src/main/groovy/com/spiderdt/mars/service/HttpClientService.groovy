package com.spiderdt.mars.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.spiderdt.mars.exception.ChangePasswordException
import com.spiderdt.mars.exception.CommonException
import com.spiderdt.mars.exception.PasswordErrorException
import com.spiderdt.mars.exception.UnAuthException
import com.spiderdt.mars.util.Slog
import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.HttpResponseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.*

@Service
public class HttpClientService {


    @Autowired
    UnAuthException unAuthException

    @Autowired
    Slog slog;


    @Value('${url.chainVersion}')
    String chainVersion
    @Value('${rules.basic}')
    String basic
    @Value('${rules.scopes}')
    String scopes
    @Value('${rules.tokenExpiredTime}')
    String tokenExpiredTime

    private AsyncHTTPBuilder http = null
    private AsyncHTTPBuilder httpScheduler = null
    private String clientPortUrl = ""
    private int poolsize
    private int sleepTime = 50
    private int timeout = 1000;

    HttpClientService() {
    }

    HttpClientService(String host, int poolsize) {
        this.clientPortUrl = host
        this.poolsize = poolsize
        http = new AsyncHTTPBuilder(poolSize: this.poolsize, uri: this.clientPortUrl)
        //    http.ignoreSSLIssues()
        httpScheduler = new AsyncHTTPBuilder(poolSize: this.poolsize, uri: this.clientPortUrl)
        //   httpScheduler.ignoreSSLIssues()
    }

    private String queryUrl = clientPortUrl + "/db-api-v1/query"

//    删除chronos job
    def deleteJob(url) {
        def future = httpScheduler.request(DELETE, JSON) { req ->
            uri.path = url
            println "************* delete" + uri
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
    }
    // 创建Chronos job
    def save(String url, String json) {
        def future = httpScheduler.request(POST, JSON) { req ->
            uri.path = url
            body = json
            println "********* " + uri
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
    }
    // 创建Chronos job
    def getPost(String url, String json) {
        def future = httpScheduler.request(POST, JSON) { req ->
            uri.path = url
            body = json
            println "********* " + uri
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def jobInfo = future.get()
        println "********jobInfo is " + jobInfo
        return jobInfo;
    }
// 创建Chronos job
    def modify(String url) {
        def future = httpScheduler.request(PUT, JSON) { req ->
            uri.path = url
            println "********* " + uri
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
    }

    // 存储并返回结果
    def querySQL(String url, String name, String sql) {
        def future = httpScheduler.request(POST, JSON) { req ->
            uri.path = url
            body = [name: name,
                    sql : sql]
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def jobInfo = future.get()
        return jobInfo;
    }

    def query(String url) {
        def future = httpScheduler.request(GET, JSON) { req ->
            uri.path = URLDecoder.decode(url, "utf-8")
            println "************* query " + uri
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def jobInfo = future.get()
        return jobInfo;
    }

    // 列出所有的信息
    def index(String url) {
        return query(url);
    }

    // 列出指定的单条信息
    def show(String url) {
        return query(url);
    }

//    def getData(String job_id, String sql) {
//        def info = querySQL(queryUrl, job_id, sql);
//        def data = info.get("data");
//        def data1 = data.get(0);
//        String str = data1.get(0) + "";
//        return str;
//    }

    // get json 请求
    def getData1(url, json) throws Exception {

        def future = httpScheduler.request(XGET, JSON) { req ->
            uri.path = url
            body = json
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def jobInfo = future.get()
        return jobInfo;
    }

    def getList(String job_id, String sql) {
        ObjectMapper om = new ObjectMapper();
        def info = querySQL(queryUrl, job_id, sql);
        def data = info.get("data");
        return data;
    }

//    def getObjectList(String job_id, String sql) {
//        ObjectMapper om = new ObjectMapper();
//        def info = querySQL(queryUrl, job_id, sql);
//
//        def data = info.get("data");
//        return data;
//    }

    def getDataset(String url) {
        ObjectMapper om = new ObjectMapper();
        def info = query(url);

        return info;
    }

//    /**
//     * 根据用户名和密码创建 token
//     * @param username
//     * @param password
//     * @return
//     */
//    def createTokenByPassword(String username, String password) {
//        HashMap header = new HashMap();
//        header.'Authorization' = "Basic " + basic.bytes.encodeBase64().toString()
//        httpClient.setHeaders(header)
//        slog.info("createTokenByPassword headers:" + header)
//        def future = httpClient.request(POST, JSON) { req ->
//            uri.path = chainVersion + '/token'
//            body = [scopes   : scopes,
//                    auth_type: "password",
//                    username : username,
//                    password : password]
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//        def result = null
//        try {
//            result = future.get()
//        } catch (HttpResponseException e) {
//            // 当用户不存在时，chain-api 会返回此错误。
//            e.printStackTrace()
//            // 使异常信息 chain-api 的错误信息一致
//            result = [status: 'error', body: "user[$username] auth failed!"]
//        } catch (Exception e) {
//            throw new CommonException(e.message)
//        }
//
//        // 异常处理
//        if (result.status == 'error') {
//            throw new PasswordErrorException(result.body)
//        }
//
//        result
//    }

    def createNewUser(String username, String password, full_name, contact, roles, data_source) {

        def future = http.request(POST, JSON) { req ->
            uri.path = '/chain-api/users'
            body = [user_id    : username,
                    full_name  : full_name,
                    password   : password,
                    contact    : contact,
                    roles      : roles,
                    data_source: data_source]
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
    }
//    /**
//     * 更新 token 过期时间
//     * 设置为目前时间 + 1800 秒，即30分钟
//     * @param token
//     */
//    void updateTokenLiveTime(String token) {
//
//        slog.info("----- updateTokenLiveTime() token: $token + $tokenExpiredTime s")
//
//        HashMap header = new HashMap()
//        header.'Authorization' = "Bearer " + token
//        httpClient.setHeaders(header)
//
//        def future = httpClient.request(PUT, JSON) { req ->
//            uri.path = "$chainVersion/token/$token/live/$tokenExpiredTime"
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//        try {
//            def result = future.get()
//            slog.info("Update $token live time status: $result")
//        } catch (Exception e) {
//            slog.info("----- updateTokenLiveTime() Failed: $e.message")
//        }
//
//    }

    void updateTokenLiveTimeByChain(String token) {

        HashMap header = new HashMap()
        header.'Authorization' = "Bearer " + token
        http.setHeaders(header)

        def future = http.request(PUT, JSON) { req ->
            uri.path = "/chain-api/token/$token/live/1800"
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        try {
            def result = future.get()
            slog.info("Update $token live time status: $result")
        } catch (Exception e) {
            slog.info("----- updateTokenLiveTime() Failed: $e.message")
        }

    }

    /**
     * 更改用户密码
     * @param token
     * @param cpv 更改用户密码值对象，包含 oldPassword 和 newPassword 属性
     */
//    def changePassword(String token, ChangePasswordVo cpv) {
//        HashMap header = new HashMap()
//        header.'Authorization' = "Bearer " + token
//        httpClient.setHeaders(header)
//        def future = httpClient.request(PUT, JSON) { req ->
//            uri.path = chainVersion + '/users/' + cpv.username + '/password'
//            body = [
//                    'old-password': cpv.oldPassword,
//                    'new-password': cpv.newPassword
//            ]
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//
//        def result = null
//        try {
//            result = future.get()
//        } catch (Exception e) {
//            throw new CommonException(e.message)
//        }
//
//
//        slog.info("changePassword result: $result")
//
//        // 异常处理
//        if (result.status == 'error') {
//            if (result.body == "token[$token] expired!") {
//                throw unAuthException
//            } else if (result.body == "confirm password!") {
//                throw new ChangePasswordException(ChangePasswordExceptionEnum.SAME_AS_BEFORE.status,
//                        ChangePasswordExceptionEnum.SAME_AS_BEFORE.desc)
//            } else {
//                throw new CommonException(result.body)
//            }
//        }
//
//        result
//    }

    /**
     *{"status":"error","body":"token[ad7feade-1ed1-49b4-82af-755e2115f88c] expired!"}* 验证 token 是否过期
     * @param token
     * @return
     */
//    def boolean validateToken(String token) {
//
//        def result = getUserInfoByToken(token)
//        if ("error".equals(result.status)) {
//            return false
//        }
//        return true
//    }
//
//    def getUserInfoByToken(String token) {
//        HashMap header = new HashMap();
//        header.'Authorization' = "Bearer " + token
//        httpClient.setHeaders(header)
//        def future = httpClient.request(GET, JSON) { req ->
//            uri.path = chainVersion + '/token/' + token
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//
//        def result = [status: "error"]
//        try {
//            result = future.get()
//        } catch (Exception e) {
//            throw new CommonException(e.message)
//        }
//        result
//    }
//
//    def logoutByToken(String token) {
//        HashMap header = new HashMap();
//        header.'Authorization' = "Bearer " + token
//        httpClient.setHeaders(header)
//
//        def future = httpClient.request(PUT, JSON) { req ->
//            uri.path = chainVersion + '/token/' + token
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//        def result = null
//        try {
//            result = future.get()
//        } catch (Exception e) {
//            throw new CommonException(e.message)
//        }
//        result
//    }
//
//    /**
//     * 获取获取距离上次改密码的天数
//     * @param username
//     * @return
//     */
//    int getUserChangePasswordIntervalDays(String username, String token) {
//        HashMap header = new HashMap();
//        header.'Authorization' = "Bearer " + token
//        httpClient.setHeaders(header)
//
//        def future = httpClient.request(GET, JSON) { req ->
//            uri.path = chainVersion + '/users/' + username + '/password/last-update-time'
//        }
//        while (!future.done) {
//            Thread.sleep(sleepTime)
//        }
//        def result = null
//        try {
//            result = future.get()
//        } catch (Exception e) {
//            throw new CommonException("Is your account: \"$username\" allowed to change password ?")
//        }
//
//        // 异常处理
//        if (result.status == 'error') {
//            if (result.body == "token[$token] expired!") {
//                throw unAuthException
//            } else {
//                throw new CommonException(result.body)
//            }
//        }
//        slog.info("$result")
//        result['interval-days']
//    }
//

    def createTokenByChainApi(String username, String password, String client_id) {
        HashMap header = new HashMap();
        header.'Authorization' = "Basic " + basic.bytes.encodeBase64().toString()
        http.setHeaders(header)
        slog.info("createTokenByPassword headers:" + header)
        def future = http.request(POST, JSON) { req ->
            uri.path = "/chain-api/token"
            body = [scopes   : "API",
                    auth_type: "password",
                    user_id  : username,
                    password : password,
                    client_id: client_id
            ]
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def result = null
        try {
            result = future.get()
        } catch (HttpResponseException e) {
            // 当用户不存在时，chain-api 会返回此错误。
            e.printStackTrace()
            // 使异常信息 chain-api 的错误信息一致
            result = [status: 'error', body: "user[$username] auth failed!"]
        } catch (Exception e) {
            throw new CommonException(e.message)
        }
        //异常处理

        if (result.status == 5055) {
            throw new PasswordErrorException(result.message)
        }

        return result.data.token

    }


    def validateTokenByChain(String token) {

        def result = getUserInfoByTokenByChain(token)
        slog.info("testing token validate is " + result)
        if (result.status == 200) {
            return true
        }
        return false
    }

    def getUserInfoByTokenByChain(String token) {
        HashMap header = new HashMap();
        header.'Authorization' = "Bearer " + token
        http.setHeaders(header)
        slog.info("getUserInfoByTokenByChain begin");
        def future = http.request(GET, JSON) { req ->
            uri.path = '/chain-api/token/' + token
        }
        slog.info("getUserInfoByTokenByChain sending");
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        slog.info("getUserInfoByTokenByChain end");
        def result = [status: "error"]
        try {
            result = future.get()
        } catch (Exception e) {
            throw new CommonException(e.message)
        }
        result
    }

    def getUserAllInfoByChain(String token, username) {
        HashMap header = new HashMap();
        header.'Authorization' = "Bearer " + token
        http.setHeaders(header)
        def future = http.request(GET, JSON) { req ->
            uri.path = '/chain-api/users/' + username + '/infos'
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }

        def result = [status: "error"]
        try {
            result = future.get()
        } catch (Exception e) {
            throw new CommonException(e.message)
        }
        result
    }

    def logoutByTokenByChain(String token) {
        HashMap header = new HashMap();
        header.'Authorization' = "Bearer " + token
        http.setHeaders(header)

        def future = http.request(PUT, JSON) { req ->
            uri.path = '/chain-api/token/' + token
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }
        def result
        try {
            result = future.get()
        } catch (Exception e) {
            throw new CommonException(e.message)
        }
        result
    }


    //验证用户是否存在，是否有这个用户邮件
    def getUserNameByEmail(String email) {
        HashMap header = new HashMap()
        header.'Authorization' = "Bearer ac920fa3-dc44-4908-a02e-9dd90f8fab8a"
        http.setHeaders(header)
        slog.info("---------- getUserNameByEmail() email:$email")
        def future = http.request(GET, JSON) { req ->
            uri.path = '/chain-api/users/email/' + email
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }

        def result = null
        try {
            result = future.get()
        } catch (Exception e) {
            slog.info("------ getUserNameByEmail CommonException: $e.message")
            throw new CommonException(e.message)
        }

        slog.info("getUserNameByEmail result: $result")

        if (result.status == 'error') {
            if (result.body == "email[$email] is not exist!") {
                slog.info("------ getUserNameByEmail IllegalArgumentException: $result")
                throw new IllegalArgumentException("Unregiste email!")
            } else {
                slog.info("------ getUserNameByEmail CommonException: $e.message")
                throw new CommonException(result.body)
            }
        }
        result.data.userId
    }

    /**
     * 重置密码
     * @param userName
     * @param password
     */
    def updatePasswordByChain(String userName, String newPassword) {
        HashMap header = new HashMap()
        header.'Authorization' = "Bearer ac920fa3-dc44-4908-a02e-9dd90f8fab8a"
        http.setHeaders(header)
        slog.info("--------- updatePassword() userName:$userName, password:$newPassword")

        def future = http.request(PUT, JSON) { req ->
            uri.path = '/chain-api/users/' + userName + '/reset-password'
            body = [
                    'new-password': newPassword
            ]
        }
        while (!future.done) {
            Thread.sleep(sleepTime)
        }

        def result = null
        try {
            result = future.get()
        } catch (Exception e) {
            slog.info("------ updatePassword CommonException: $e.message")
            throw new CommonException(e.message)
        }

        slog.info("updatePasswordByEmail result: $result")

        result
    }


}
