var util = require("./util.js");
var url = require('url');
var MockRoute = function (request, response) {
    var urlPath = url.parse(request.url).pathname;
    var reqParams = url.parse(request.url, true).query;
    console.log('pathname :' + urlPath);
    console.log('query :' + JSON.stringify(reqParams));
    switch (urlPath) {
        // 邀请记录
        case "/mll-wechat/getReferees":
            util.fRestApi(response,'[{"inviteeName":"李小姐","valids":"1","image_path":"../assets/images/barcode.png","refereeDate":"2017-03-02 00:00:00","id": 1,"refereeId": "18505661228","inviteeId": "18505661228"}]');
            break;
        // 邀请码
        case "/mll-wechat/getRefereeCode":
            util.fRestApi(response,'"20180112161120362"');
        break;
        // 优惠劵列表
        case "/mll-wechat/coupons":
            util.fRestApi(response,'[{"code":"2384738574395","ruleName":"优惠券123456788","valids":"1","cst_id":"23423746234","classess":"0","balances":"234.00","image_path":"../assets/images/barcode.png","remark":"新会员注册奖励:消费满188元使用；一经使用，都不退换，消费金额满188元打9.5折。一经使用，盖不退换。","s_outdate":"2017-03-02 00:00:00","e_outdate":"2018-08-22 23:59:59"},{"code":"2384738574395","ruleName":"优惠券123456788","valids":"4","cst_id":"23423746234","classess":"0","balances":"234.00","image_path":"../assets/images/barcode.png","remark":"新会员注册奖励:消费满188元使用；一经使用，都不退换，消费金额满188元打9.5折。一经使用，盖不退换。","s_outdate":"2017-03-02 00:00:00","e_outdate":"2018-08-22 23:59:59"},{"code":"2384738574395","ruleName":"优惠券123456788","valids":"5","cst_id":"23423746234","classess":"0","balances":"234.00","image_path":"../assets/images/barcode.png","remark":"新会员注册奖励:消费满188元使用；一经使用，都不退换，消费金额满188元打9.5折。一经使用，盖不退换。","s_outdate":"2017-03-02 00:00:00","e_outdate":"2018-08-22 23:59:59"}]');
            break;
        // 个人信息
        case "/mll-wechat/getCustomerInfo":
            util.fRestApi(response,'{"openId":"12312312","fullName":"test1","telephone":"15298376475","nickName":"昵称hello","gender":"女","birth":"2013-09-12","avocation":"茶艺茶道,烹饪厨艺","height":"165","weight":"100.0","province":"610000","city":"610400"}');
            break;
        // 省份
        case "/mll-wechat/getAreas":
            util.fRestApi(response,'[{"id":"110000","name":"北京","parent_id":"4","type":1},{"id":"120000","name":"天津","parent_id":"4","type":1},{"id":"130000","name":"河北","parent_id":"4","type":1},{"id":"610000","name":"陕西","parent_id":"5","type":1}]');
            break;
        // 城市
        case "/mll-wechat/getAreas2":
            util.fRestApi(response,'[{"id":"610100","name":"西安市","parent_id":"610000","type":2},{"id":"610200","name":"铜川市","parent_id":"610000","type":2},{"id":"610300","name":"宝鸡市","parent_id":"610000","type":2},{"id":"610400","name":"咸阳市","parent_id":"610000","type":2},{"id":"610500","name":"渭南市","parent_id":"610000","type":2}]');
            break;
        case "/mll-wechat/updateInfo":
            util.fRestApi(response,'[]');
            break;
        default:
            console.log('{"msg":"该方法尚未实现，请补充","path":"' + urlPath + '"}');
            util.fRestApi(response, '{"msg":"该方法尚未实现，请补充","path":"' + urlPath + '"}');
            break;
    }
}
module.exports = MockRoute;