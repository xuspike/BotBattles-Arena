import $ from 'jquery';
export default {
    state: {
        id: "",
        username: "",
        photo: "",
        resume: "这个人没有留下任何内容~",
        noticeCount: 0,
        friendships: null,
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            if(user.resume != null) state.resume = user.resume;
            state.noticeCount = user.noticeCount;
            state.friendships = user.friendships;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.id = "";
            state.username = "";
            state.token = "";
            state.photo = "";
            state.is_login = false;
        },
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "https://xrookie.xyz/api/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(resp) {
                    if(resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success();
                    } else {
                        data.error();
                    }
                },
                error(resp) {
                    data.error(resp);
                },
              });
        },
        getinfo(context, data) {
            $.ajax({
            url: "https://xrookie.xyz/api/user/account/info/",
            type: "get",
            headers: {
                Authorization:
                "Bearer " + context.state.token,
            },
            success(resp) {
                if(resp.error_message === "success") {
                    context.commit("updateUser", {
                        ...resp,
                        is_login: true,
                    })
                    data.success(resp);
                } else {
                    data.error(resp);
                }
            },
            error(resp) {
                data.error(resp);
            },
            });
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
}