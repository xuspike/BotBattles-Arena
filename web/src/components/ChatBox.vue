<template>
  <div class="container">
    <div class="row">
      <section class="discussions">
        <el-scrollbar height="400px">
          <div class="discussion search">
            <div class="searchbar">
              <el-icon><Search /></el-icon>
              <input type="text" placeholder="Search..." />
            </div>
            <el-icon
              style="
                font-size: 30px;
                margin-left: 1vw;
                color: lightblue;
                cursor: pointer;
              "
              @click="addFriendVisable = true"
              ><CirclePlus
            /></el-icon>
          </div>
          <el-dialog v-model="addFriendVisable" title="添加好友" width="40%">
            <div
              class="makeFriend"
              v-infinite-scroll="load_searchUsers"
              :infinite-scroll-disabled="search_disabled"
              infinite-scroll-distance="1"
            >
              <el-row>
                <el-col :span="20"
                  ><el-input
                    style="margin-left: 1vw"
                    v-model="search_username"
                    placeholder="请输入你要搜寻的用户名"
                /></el-col>
                <el-col :span="4">
                  <el-button
                    @click="reset_userSearch"
                    style="margin-left: 20px"
                    :icon="Search"
                    circle
                  />
                </el-col>
              </el-row>
              <div class="userMessage" v-for="user in users" :key="user.id">
                <el-row>
                  <el-col :span="6">
                    <el-avatar
                      style="margin-left: 5px"
                      :src="user.photo"
                      :size="30"
                    ></el-avatar>
                  </el-col>
                  <el-col :span="1"></el-col>
                  <el-col :span="13">
                    <div style="color: gray; font-size: 9pt">
                      {{ user.username }}
                    </div>
                    <el-tooltip
                      v-if="user.resume != null"
                      :content="user.resume"
                      placement="bottom"
                      effect="light"
                    >
                      <div class="user-resume" title="user.resume">
                        {{ user.resume }}
                      </div>
                    </el-tooltip>
                    <el-tooltip
                      v-else
                      content="这个人没有留下任何内容"
                      placement="bottom"
                      effect="light"
                    >
                      <div class="user-resume">这个人没有留下任何内容~</div>
                    </el-tooltip>
                  </el-col>
                  <el-col
                    :span="4"
                    style="
                      font-size: 25px;
                      display: grid;
                      place-items: center; /* 水平垂直居中 */
                    "
                  >
                    <el-icon
                      v-if="user.isSended == false && !friendSet.has(user.id)"
                      style="cursor: pointer"
                      @click="sendFriendNotice(user)"
                      ><CirclePlus
                    /></el-icon>
                    <el-icon
                      v-else-if="
                        user.isSended == true && !friendSet.has(user.id)
                      "
                      ><CircleCheck
                    /></el-icon>
                  </el-col>
                </el-row>
              </div>
              <div v-loading="search_loading"></div>
            </div>
          </el-dialog>

          <!-- class = "message-active"表示选中 -->
          <div v-for="friend in friends" class="discussion">
            <div
              class="photo"
              :style="'background-image: url(' + friend.friend.photo + ')'"
            ></div>
            <div class="desc-contact">
              <p class="name">{{ friend.friend.username }}</p>
              <p class="message" v-if="friend.friendship.lastMsgId != -1">
                {{ friend.lastMessage.content }}
              </p>
              <p class="message" v-else>和新朋友开始聊天吧~</p>
            </div>
            <div class="timer" v-if="friend.friendship.lastMsgId != -1">
              {{ friend.lastMessage.createtime }}
            </div>
          </div>
          <div class="discussion message-active">
            <div
              class="photo"
              style="
                background-image: url(https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80);
              "
            >
              <div class="online"></div>
            </div>
            <div class="desc-contact">
              <p class="name">Megan Leib</p>
              <p class="message">9 pm at the bar if possible 😳</p>
            </div>
            <div class="timer">12 sec</div>
          </div>
        </el-scrollbar>
      </section>
      <section class="chat">
        <div class="header-chat">
          <i class="icon fa fa-user-o" aria-hidden="true"></i>
          <p class="name">Megan Leib</p>
          <i
            class="icon clickable fa fa-ellipsis-h right"
            aria-hidden="true"
          ></i>
        </div>
        <div class="messages-chat">
          <div class="message">
            <div
              class="photo"
              style="
                background-image: url(https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80);
              "
            ></div>
            <p class="text">Hi, how are you ?</p>
          </div>
          <div class="message text-only">
            <p class="text">
              What are you doing tonight ? Want to go take a drink ?
            </p>
          </div>
          <p class="time">14h58</p>
          <div class="message text-only">
            <div class="response">
              <p class="text">Hey Megan ! It's been a while 😃</p>
            </div>
          </div>
          <div class="message text-only">
            <div class="response">
              <p class="text">When can we meet ?</p>
            </div>
          </div>
          <p class="response-time time">15h04</p>
          <div class="message">
            <div
              class="photo"
              style="
                background-image: url(https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80);
              "
            ></div>
            <p class="text">9 pm at the bar if possible 😳</p>
          </div>
          <p class="time">15h09</p>
        </div>
        <div class="footer-chat">
          <svg
            style="margin-right: 1vw"
            t="1706541978834"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1462"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            width="35"
            height="35"
          >
            <path
              d="M288 421a48 48 0 1 0 96 0 48 48 0 1 0-96 0z m352 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0zM512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z m263 711c-34.2 34.2-74 61-118.3 79.8C611 874.2 562.3 884 512 884c-50.3 0-99-9.8-144.8-29.2-44.3-18.7-84.1-45.6-118.3-79.8-34.2-34.2-61-74-79.8-118.3C149.8 611 140 562.3 140 512s9.8-99 29.2-144.8c18.7-44.3 45.6-84.1 79.8-118.3 34.2-34.2 74-61 118.3-79.8C413 149.8 461.7 140 512 140c50.3 0 99 9.8 144.8 29.2 44.3 18.7 84.1 45.6 118.3 79.8 34.2 34.2 61 74 79.8 118.3C874.2 413 884 461.7 884 512s-9.8 99-29.2 144.8c-18.7 44.3-45.6 84.1-79.8 118.2zM664 533h-48.1c-4.2 0-7.8 3.2-8.1 7.4C604 589.9 562.5 629 512 629s-92.1-39.1-95.8-88.6c-0.3-4.2-3.9-7.4-8.1-7.4H360c-4.6 0-8.2 3.8-8 8.4 4.4 84.3 74.5 151.6 160 151.6s155.6-67.3 160-151.6c0.2-4.6-3.4-8.4-8-8.4z"
              fill="#1296DB"
              p-id="1463"
            ></path>
          </svg>
          <el-input
            v-model="textarea"
            :rows="2"
            type="textarea"
            placeholder="Please input"
          />
          <el-icon
            style="
              padding: 4px 4px 4px 4px;
              margin-left: 1vw;
              font-size: 35px;
              background-color: lightskyblue;
              border-radius: 50px;
            "
            ><Promotion
          /></el-icon>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { Search } from "@element-plus/icons-vue";
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import $ from "jquery";
export default {
  setup() {
    const store = useStore();
    const friendSet = new Set();

    const search_loading = ref(false);
    const search_disabled = ref(false);
    const addFriendVisable = ref(false);
    const search_username = ref("");
    const users = ref([]);

    const friends = ref([]);

    let search_currentPage = 0;

    const load_searchUsers = () => {
      if (!search_loading.value && !search_disabled.value) {
        search_loading.value = true;
        pull_SearchUsers();
      }
    };

    const pull_SearchUsers = () => {
      search_currentPage += 1;
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/search/",
        type: "get",
        data: {
          username: search_username.value,
          page: search_currentPage,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            for (let i = 0; i < resp.users.length; i++) {
              resp.users[i].isSended = false;
            }
            users.value = users.value.concat(resp.users);
            if (resp.usersCount / 9 <= search_currentPage)
              search_disabled.value = true;
            search_loading.value = false;
          }
        },
      });
    };

    const reset_userSearch = () => {
      search_currentPage = 0;
      users.value = [];
      pull_SearchUsers();
    };

    const sendFriendNotice = (user) => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/friend/notice/send/",
        type: "post",
        data: {
          senderId: store.state.user.id,
          receiverId: user.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            user.isSended = true;
            ElMessage({
              showClose: true,
              message: "已发送请求~",
              type: "success",
            });
          }
        },
      });
    };

    const pull_friends = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/friend/getlist/",
        type: "get",
        data: {
          userId: store.state.user.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            console.log(resp);
            friends.value = resp.friends;
          }
        },
      });
    };

    onMounted(() => {
      friendSet.add(parseInt(store.state.user.id));
      for (let i = 0; i < store.state.user.friendships.length; i++) {
        friendSet.add(store.state.user.friendships[i]);
      }
      pull_friends();
    });
    return {
      store,
      search_disabled,
      search_loading,
      users,
      search_username,
      addFriendVisable,
      friendSet,
      friends,
      load_searchUsers,
      pull_SearchUsers,
      reset_userSearch,
      sendFriendNotice,
      Search,
    };
  },
};
</script>

<style scoped>
.makeFriend {
  height: 270px;
  overflow: auto;
  list-style: none;
}

.user-resume {
  color: #515151;
  font-size: 12px;
  white-space: nowrap; /* 保持文本在一行内显示 */
  overflow: hidden; /* 隐藏超出容器的内容 */
  text-overflow: ellipsis; /* 使用省略号表示被裁剪的文本 */
  height: 14px;
}
.userMessage {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-left: 5px;
  margin-top: 5px;
  float: left;
  width: 30%;
  height: 30%;
  background-color: #fafafa;
  border: 1px solid #bbb;
  border-radius: 5px;
}
.container {
  padding: 0;
  background-color: #fff;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23);
  height: 400px;
}

/* ===== MENU ===== */
.menu {
  float: left;
  height: 400px;
  width: 40px;
  background: #4768b5;
  background: -webkit-linear-gradient(#4768b5, #35488e);
  background: -o-linear-gradient(#4768b5, #35488e);
  background: -moz-linear-gradient(#4768b5, #35488e);
  background: linear-gradient(#4768b5, #35488e);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19);
}

.menu .items {
  list-style: none;
  margin: auto;
  padding: 0;
}

.menu .items .item {
  height: 40px;
  border-bottom: 1px solid #6780cc;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #9fb5ef;
  font-size: 17pt;
}

.menu .items .item-active {
  background-color: #5172c3;
  color: #fff;
}

.menu .items .item:hover {
  cursor: pointer;
  background-color: #4f6ebd;
  color: #cfe5ff;
}

/* === CONVERSATIONS === */

.discussions {
  width: 25%;
  height: 400px;
  box-shadow: 0px 8px 10px rgba(0, 0, 0, 0.2);
  overflow: auto;
  display: inline-block;
}

.discussions .discussion {
  width: 100%;
  height: 70px;
  background-color: #fafafa;
  border-bottom: solid 1px #e0e0e0;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.discussions .search {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #e0e0e0;
}

.discussions .search .searchbar {
  height: 40px;
  background-color: #fff;
  width: 70%;
  padding: 0 20px;
  border-radius: 50px;
  border: 1px solid #eeeeee;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.discussions .search .searchbar input {
  margin-left: 15px;
  height: 38px;
  width: 100%;
  border: none;
  font-family: "Montserrat", sans-serif;
}

.discussions .search .searchbar *::-webkit-input-placeholder {
  color: #e0e0e0;
}
.discussions .search .searchbar input *:-moz-placeholder {
  color: #e0e0e0;
}
.discussions .search .searchbar input *::-moz-placeholder {
  color: #e0e0e0;
}
.discussions .search .searchbar input *:-ms-input-placeholder {
  color: #e0e0e0;
}

.discussions .message-active {
  width: 98.5%;
  height: 70px;
  background-color: #fff;
  border-bottom: solid 1px #e0e0e0;
}

.discussions .discussion .photo {
  margin-left: 20px;
  display: block;
  width: 40px;
  height: 40px;
  background: #e6e7ed;
  border-radius: 50px;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}

.online {
  position: relative;
  top: 30px;
  left: 35px;
  width: 13px;
  height: 13px;
  background-color: #8bc34a;
  border-radius: 13px;
  border: 3px solid #fafafa;
}

.desc-contact {
  height: 43px;
  width: 50%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.discussions .discussion .name {
  margin: 0 0 0 20px;
  font-family: "Montserrat", sans-serif;
  font-size: 9pt;
  color: gray;
}

.discussions .discussion .message {
  margin: 6px 0 0 20px;
  font-family: "Montserrat", sans-serif;
  font-size: 8pt;
  color: #515151;
}

.timer {
  margin-left: 5%;
  font-family: "Open Sans", sans-serif;
  font-size: 9px;
  padding: 3px 3px;
  color: #bbb;
  background-color: #fff;
  border: 1px solid #e5e5e5;
  border-radius: 15px;
}

.chat {
  width: 73%;
}

.header-chat {
  background-color: #fff;
  height: 60px;
  box-shadow: 0px 3px 2px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.chat .header-chat .icon {
  margin-left: 30px;
  color: #515151;
  font-size: 8pt;
}

.chat .header-chat .name {
  margin: 0 0 0 20px;
  text-transform: uppercase;
  font-family: "Montserrat", sans-serif;
  font-size: 10pt;
  color: #515151;
}

.chat .header-chat .right {
  position: absolute;
  right: 40px;
}

.chat .messages-chat {
  padding: 10px 15px;
}

.chat .messages-chat .message {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  overflow: hidden !important; /* 隐藏超出容器的内容 */
  text-overflow: ellipsis !important; /* 使用省略号表示被裁剪的文本 */
}

.chat .messages-chat .message .photo {
  display: block;
  width: 30px;
  height: 30px;
  background: #e6e7ed;
  border-radius: 50px;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}

.chat .messages-chat .text {
  margin: 0 10px;
  background-color: #f6f6f6;
  padding: 10px;
  border-radius: 12px;
  font-size: 12px;
}

.text-only {
  margin-left: 30px;
}

.time {
  font-size: 10px;
  color: lightgrey;
  margin-bottom: 10px;
  margin-left: 85px;
}

.response-time {
  float: right;
  margin-right: 40px !important;
}

.response {
  float: right;
  margin-right: 0px !important;
  margin-left: auto; /* flexbox alignment rule */
}

.response .text {
  background-color: #e3effd !important;
}

.footer-chat {
  width: 73%;
  height: 70px;
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 0;
  background-color: transparent;
  border-top: 2px solid #eee;
}

.chat .footer-chat .icon {
  margin-left: 10px;
  color: #c0c0c0;
  font-size: 12pt;
}

.chat .footer-chat .send {
  color: #fff;
  background-color: #4f6ebd;
  position: absolute;
  right: 50px;
  padding: 12px 12px 12px 12px;
  border-radius: 50px;
  font-size: 9pt;
}

.chat .footer-chat .name {
  margin: 0 0 0 20px;
  text-transform: uppercase;
  font-family: "Montserrat", sans-serif;
  font-size: 13pt;
  color: #515151;
}

.chat .footer-chat .right {
  position: absolute;
  right: 40px;
}

.write-message {
  border: none !important;
  width: 60%;
  height: 50px;
  margin-left: 20px;
  padding: 10px;
}

.footer-chat *::-webkit-input-placeholder {
  color: #c0c0c0;
  font-size: 13pt;
}
.footer-chat input *:-moz-placeholder {
  color: #c0c0c0;
  font-size: 13pt;
}
.footer-chat input *::-moz-placeholder {
  color: #c0c0c0;
  font-size: 13pt;
  margin-left: 5px;
}
.footer-chat input *:-ms-input-placeholder {
  color: #c0c0c0;
  font-size: 13pt;
}

.clickable {
  cursor: pointer;
}
</style>
