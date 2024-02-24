import $ from 'jquery';
export default {
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
    uploadImage(context, data) {
        //填写获取签名的地址
        const getPolicyApiUrl = 'https://app6102.acapp.acwing.com.cn/api/ali/oss/policy/'; //获取oss签名的地址
        $.ajax({
            url: getPolicyApiUrl, 
            type: "GET",
            headers: {
                Authorization:
                "Bearer " + context.rootState.user.token,
            },
            data: {
                'filename': data.file.name,
            },
            success: resp => {
                let ossUrl = "https://xrookie.oss-cn-hangzhou.aliyuncs.com";
                let pos = data.file.name.lastIndexOf('.');
                let fileName = ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c => (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)) + data.file.name.substr(pos);
                let accessUrl = resp.dir + '/' + fileName;
                let sendData = new FormData();
                sendData.append("OSSAccessKeyId", resp.ossAccessKeyId);
                sendData.append("policy", resp.policy);
                sendData.append("signature", resp.signature);
                sendData.append("keys", resp.dir);
                sendData.append("callback", resp.callback);
                sendData.append("key", accessUrl);
                sendData.append('success_action_status', 200); // 指定返回的状态码
                sendData.append('type', data.file.type);
                sendData.append('file', data.file);
                $.ajax({
                    url: ossUrl, 
                    type: "POST",
                    cache: false,
                    contentType: false,
                    processData: false,
                    data: sendData,
                    success(resp) {
                        console.log(resp);
                        data.success(resp.imgUrl, fileName);
                    },
                    error() {
                        console.log("上传失败！");
                    }
                });
            },
            error() {
                console.log("上传失败！");
            }
        });
    },

  },
  modules: {
  }
}
