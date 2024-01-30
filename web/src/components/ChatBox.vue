<template>
  <div class="container">
    <div class="row">
      <section class="discussions">
        <div class="discussion search">
          <div class="searchbar">
            <el-icon><Search /></el-icon>
            <input type="text" placeholder="Search..." />
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

        <div class="discussion">
          <div
            class="photo"
            style="
              background-image: url(https://i.pinimg.com/originals/a9/26/52/a926525d966c9479c18d3b4f8e64b434.jpg);
            "
          >
            <div class="online"></div>
          </div>
          <div class="desc-contact">
            <p class="name">Dave Corlew</p>
            <p class="message">Let's meet for a coffee or something today ?</p>
          </div>
          <div class="timer">3 min</div>
        </div>

        <div class="discussion">
          <div
            class="photo"
            style="
              background-image: url(https://images.unsplash.com/photo-1497551060073-4c5ab6435f12?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=667&q=80);
            "
          ></div>
          <div class="desc-contact">
            <p class="name">Jerome Seiber</p>
            <p class="message">I've sent you the annual report</p>
          </div>
          <div class="timer">42 min</div>
        </div>

        <div class="discussion">
          <div
            class="photo"
            style="
              background-image: url(https://card.thomasdaubenton.com/img/photo.jpg);
            "
          >
            <div class="online"></div>
          </div>
          <div class="desc-contact">
            <p class="name">Thomas Dbtn</p>
            <p class="message">See you tomorrow ! 🙂</p>
          </div>
          <div class="timer">2 hour</div>
        </div>

        <div class="discussion">
          <div
            class="photo"
            style="
              background-image: url(https://images.unsplash.com/photo-1553514029-1318c9127859?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=700&q=80);
            "
          ></div>
          <div class="desc-contact">
            <p class="name">Elsie Amador</p>
            <p class="message">What the f**k is going on ?</p>
          </div>
          <div class="timer">1 day</div>
        </div>

        <div class="discussion">
          <div
            class="photo"
            style="
              background-image: url(https://images.unsplash.com/photo-1541747157478-3222166cf342?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=967&q=80);
            "
          ></div>
          <div class="desc-contact">
            <p class="name">Billy Southard</p>
            <p class="message">Ahahah 😂</p>
          </div>
          <div class="timer">4 days</div>
        </div>
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
            >
              <div class="online"></div>
            </div>
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
            >
              <div class="online"></div>
            </div>
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
            width="40"
            height="40"
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
              font-size: 40px;
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
import { onMounted } from "vue";
export default {
  setup() {
    onMounted(() => {});
  },
};
</script>

<style scoped>
.container {
  padding: 0;
  background-color: #fff;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23);
  height: 700px;
}

/* ===== MENU ===== */
.menu {
  float: left;
  height: 700px;
  width: 70px;
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
  height: 70px;
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
  width: 35%;
  height: 700px;
  box-shadow: 0px 8px 10px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  display: inline-block;
}

.discussions .discussion {
  width: 100%;
  height: 90px;
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
  height: 90px;
  background-color: #fff;
  border-bottom: solid 1px #e0e0e0;
}

.discussions .discussion .photo {
  margin-left: 20px;
  display: block;
  width: 45px;
  height: 45px;
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
  font-size: 11pt;
  color: #515151;
}

.discussions .discussion .message {
  margin: 6px 0 0 20px;
  font-family: "Montserrat", sans-serif;
  font-size: 9pt;
  color: #515151;
}

.timer {
  margin-left: 15%;
  font-family: "Open Sans", sans-serif;
  font-size: 11px;
  padding: 3px 8px;
  color: #bbb;
  background-color: #fff;
  border: 1px solid #e5e5e5;
  border-radius: 15px;
}

.chat {
  width: calc(65% - 85px);
}

.header-chat {
  background-color: #fff;
  height: 80px;
  box-shadow: 0px 3px 2px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.chat .header-chat .icon {
  margin-left: 30px;
  color: #515151;
  font-size: 14pt;
}

.chat .header-chat .name {
  margin: 0 0 0 20px;
  text-transform: uppercase;
  font-family: "Montserrat", sans-serif;
  font-size: 13pt;
  color: #515151;
}

.chat .header-chat .right {
  position: absolute;
  right: 40px;
}

.chat .messages-chat {
  padding: 25px 35px;
}

.chat .messages-chat .message {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.chat .messages-chat .message .photo {
  display: block;
  width: 45px;
  height: 45px;
  background: #e6e7ed;
  border-radius: 50px;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}

.chat .messages-chat .text {
  margin: 0 35px;
  background-color: #f6f6f6;
  padding: 15px;
  border-radius: 12px;
}

.text-only {
  margin-left: 45px;
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
  width: calc(65% - 66px);
  height: 100px;
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 0;
  background-color: transparent;
  border-top: 2px solid #eee;
}

.chat .footer-chat .icon {
  margin-left: 30px;
  color: #c0c0c0;
  font-size: 14pt;
}

.chat .footer-chat .send {
  color: #fff;
  background-color: #4f6ebd;
  position: absolute;
  right: 50px;
  padding: 12px 12px 12px 12px;
  border-radius: 50px;
  font-size: 14pt;
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
