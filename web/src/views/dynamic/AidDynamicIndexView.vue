<!-- 指定的动态页面 -->
<template>
  <ContentField v-if="dynamic.is_rendered" style="width: 60%; color: white">
    <el-row>
      <el-col :span="2">
        <el-avatar :size="45" :src="dynamic.parent.userPhoto"></el-avatar>
      </el-col>
      <e-col :span="4">
        <div style="margin-left: 1vw">{{ dynamic.parent.username }}</div>
        <div style="margin-left: 1vw; color: gray; font-size: small">
          {{ dynamic.parent.pastTime }}
        </div>
      </e-col>
    </el-row>
    <el-row>
      <el-col :span="2"></el-col>
      <el-col :span="22">
        <pre
          class="parent-content"
          :id="'parent-content' + dynamic.parent.id"
          v-html="dynamic.parent.content"
          style="line-height: 1; white-space: pre-line"
        ></pre>
      </el-col>
    </el-row>
    <el-divider />
    <el-row>
      <el-col :span="6"></el-col>
      <el-col
        :span="2"
        style="text-align: center; color: gray; cursor: pointer"
        @click="GiveLike(dynamic.parent)"
      >
        <svg
          class="bi bi-heart"
          width="1em"
          height="1em"
          viewBox="0 0 16 16"
          fill="currentColor"
          xmlns="http://www.w3.org/2000/svg"
          :class="{ 'icon-red': dynamic.parent.isIconClicked }"
        >
          <path
            fill-rule="evenodd"
            d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"
          />
        </svg>
        &nbsp;{{ dynamic.parent.likes }}
      </el-col>
      <el-col :span="8"></el-col>
      <el-col
        :span="2"
        style="text-align: center; color: gray; cursor: pointer"
        @click="openPostContent(dynamic)"
        ><svg
          class="bi bi-chat-square-dots"
          width="1em"
          height="1em"
          viewBox="0 0 16 16"
          fill="currentColor"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            fill-rule="evenodd"
            d="M14 1H2a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h2.5a2 2 0 0 1 1.6.8L8 14.333 9.9 11.8a2 2 0 0 1 1.6-.8H14a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 0a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h2.5a1 1 0 0 1 .8.4l1.9 2.533a1 1 0 0 0 1.6 0l1.9-2.533a1 1 0 0 1 .8-.4H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"
          />
          <path
            d="M5 6a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"
          />
        </svg>
        &nbsp;{{ dynamic.childrenCnt }}</el-col
      >
      <el-col :span="6"></el-col>
    </el-row>
    <div :class="{ expand: dynamic.foldPostVisible }" class="fold-post">
      <div style="font-size: large; font-weight: bold; margin-bottom: 1vh">
        评论({{ dynamic.childrenCnt }})
      </div>
      <el-row>
        <el-col :span="2">
          <el-avatar :size="40" :src="store.state.user.photo" />
        </el-col>
        <el-col :span="19">
          <el-input
            v-model="dynamic.textarea"
            autosize:autosize="{ minRows: 2, maxRows: 10 }"
            type="textarea"
            placeholder="畅所欲言吧！"
          />
        </el-col>
        <el-col :span="3">
          <el-button
            type="info"
            round
            style="margin-top: 3.5vh; margin-left: 0.8vw"
            @click="create_dynamic(dynamic, dynamic.parent.id)"
            >评论</el-button
          >
        </el-col>
      </el-row>
      <div
        class="child-dynamic"
        v-for="child in dynamic.children"
        :key="child.id"
        style="margin-top: 3vh"
      >
        <el-row>
          <el-col :span="2"></el-col>
          <el-col :span="1">
            <el-avatar :size="30" :src="child.userPhoto"></el-avatar>
          </el-col>
          <el-col :span="15">
            <div style="margin-left: 1vw; font-size: 12px">
              {{ child.username }}
              &nbsp;<a
                v-if="child.replyId != dynamic.parent.id"
                style="color: #03658c; text-decoration: none"
                href="javascript:void(0)"
                >回复</a
              >
              <span v-if="child.replyId != dynamic.parent.id"
                >&nbsp;&nbsp;{{ child.replyName }}</span
              >
            </div>
            <div style="margin-left: 1vw; color: gray; font-size: 12px">
              {{ child.pastTime }}
            </div>
          </el-col>
          <el-col :span="2" style="font-size: 12px"
            ><svg
              class="bi bi-heart"
              width="1em"
              height="1em"
              viewBox="0 0 16 16"
              fill="currentColor"
              xmlns="http://www.w3.org/2000/svg"
              :class="{ 'icon-red': child.isIconClicked }"
              @click="GiveLike(child)"
              style="cursor: pointer"
            >
              <path
                fill-rule="evenodd"
                d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"
              />
            </svg>
            &nbsp;{{ child.likes }}
          </el-col>
          <el-col
            :span="2"
            style="font-size: 12px; cursor: pointer"
            @click="openPostContent(child)"
          >
            <svg
              class="bi bi-chat-square-dots"
              width="1em"
              height="1em"
              viewBox="0 0 16 16"
              fill="currentColor"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fill-rule="evenodd"
                d="M14 1H2a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h2.5a2 2 0 0 1 1.6.8L8 14.333 9.9 11.8a2 2 0 0 1 1.6-.8H14a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 0a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h2.5a1 1 0 0 1 .8.4l1.9 2.533a1 1 0 0 0 1.6 0l1.9-2.533a1 1 0 0 1 .8-.4H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"
              />
              <path
                d="M5 6a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"
              />
            </svg>
            &nbsp;回复
          </el-col>
          <el-col
            :span="2"
            style="text-align: center"
            v-if="store.state.user.id == child.userId"
            ><el-icon
              color="red"
              style="cursor: pointer"
              @click="child.centerDialogVisible = true"
              ><Delete /></el-icon
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="3"></el-col>
          <el-col :span="21">
            <pre
              style="
                margin-bottom: 0;
                line-height: 1;
                margin-left: 1vw;
                white-space: pre-line;
              "
              v-html="child.content"
            ></pre>
          </el-col>
        </el-row>

        <div
          class="child-input"
          :class="{ 'child-expend': child.foldPostVisible }"
        >
          <el-row>
            <el-col :span="2"></el-col>
            <el-col :span="19">
              <el-input
                v-model="child.textarea"
                :autosize="{ minRows: 2, maxRows: 3 }"
                type="textarea"
                placeholder="畅所欲言吧！"
              />
            </el-col>
            <el-col :span="3">
              <el-button
                type="info"
                round
                style="margin-top: 3.5vh; margin-left: 0.8vw"
                @click="create_dynamic(child, child.id)"
                >评论</el-button
              >
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import { useRoute } from "vue-router";
import ContentField from "../../components/ContentField.vue";
import $ from "jquery";
import MarkdownIt from "markdown-it";
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const route = useRoute();
    let dynamic = ref({
      is_rendered: false,
    });
    let is_liked = [];

    // 将时间差转化
    const change_date = (current_date) => {
      // 将日期转化为时间差
      const currentDate = new Date(); // 获取当前时间的 Date 对象
      const serverDate = new Date(current_date);
      // 计算时间差（单位为毫秒）
      let timeDifference = currentDate.getTime() - serverDate.getTime();
      timeDifference /= 1000;
      let pastTime = "";

      if (timeDifference < 60) {
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "秒前";
      } else if (timeDifference < 3600) {
        timeDifference /= 60;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "分钟前";
      } else if (timeDifference < 3600 * 24) {
        timeDifference /= 3600;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "小时前";
      } else if (timeDifference < 3600 * 24 * 30) {
        timeDifference /= 3600 * 24;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "天前";
      } else if (timeDifference < 3600 * 24 * 30 * 12) {
        timeDifference /= 3600 * 24 * 30;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "月前";
      } else {
        timeDifference /= 3600 * 24 * 30 * 12;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "年前";
      }

      return pastTime;
    };

    const pull_aid_dynamic = () => {
      const dynamicId = route.params.dynamicId;

      $.ajax({
        url: "http://127.0.0.1:3000/api/dynamic/getAid/",
        type: "get",
        data: {
          dynamicId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          const md = new MarkdownIt();
          console.log(resp);

          if (resp.result === "success") {
            let pastTime = change_date(resp.parent.createtime);
            resp.parent.pastTime = pastTime;
            resp.parent.content = md.render(resp.parent.content);
            resp.foldPostVisible = false;
            resp.textarea = "";
            resp.parent.isIconClicked = false;
            resp.foldPostVisible = false;
            if (is_liked.includes(resp.parent.id))
              resp.parent.isIconClicked = true;
            for (let i = 0; i < resp.children.length; i++) {
              pastTime = change_date(resp.children[i].createtime);
              resp.children[i].pastTime = pastTime;
              resp.children[i].content = md.render(resp.children[i].content);
              resp.children[i].textarea = "";
              resp.children[i].isIconClicked = false;
              resp.children[i].foldPostVisible = false;
              if (is_liked.includes(resp.children[i].id)) {
                resp.children[i].isIconClicked = true;
              }
            }
            resp.is_rendered = true;
            dynamic.value = resp;
            console.log(dynamic);
          }
        },
      });
    };

    // 点赞或者取消点赞, 待完成
    const post_like = (dynamic, num) => {
      console.log(num);
      $.ajax({
        url: "http://127.0.0.1:3000/api/dynamic/give/like/",
        type: "post",
        data: {
          dynamicId: dynamic.id,
          num,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.result === "success") {
            if (num == 1) {
              ElMessage({
                showClose: true,
                message: "成功点赞！",
                type: "success",
              });
            } else {
              ElMessage({
                showClose: true,
                message: "成功取消！",
                type: "success",
              });
            }

            dynamic.likes += num;
          }
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    const GiveLike = (dynamic) => {
      if (is_liked.includes(dynamic.id)) {
        console.log("取消点赞");
        const index = is_liked.indexOf(dynamic.id);
        if (index > -1) {
          is_liked.splice(index, 1);
          localStorage.setItem("LikedDynamics", JSON.stringify(is_liked));
          dynamic.isIconClicked = false;
          post_like(dynamic, -1); // 取消点赞
        }
      } else {
        console.log("点赞");
        is_liked.push(dynamic.id);
        // dynamic.isIconClicked = true;
        localStorage.setItem("LikedDynamics", JSON.stringify(is_liked));
        dynamic.isIconClicked = true;
        post_like(dynamic, 1); // 点赞
      }
    };

    // 获取localStorage中点赞键值对
    const getLocalStorage = () => {
      is_liked = localStorage.getItem("LikedDynamics");
      if (is_liked == null) is_liked = [];
      if (is_liked && is_liked.length != 0) {
        is_liked = JSON.parse(is_liked);
      }
    };

    // 判断最近是否点过赞
    const isDynamicLiked = (dynamicId) => {
      return is_liked.includes(dynamicId);
    };

    // 展开或折叠发送评论
    const openPostContent = (dynamic) => {
      if (dynamic.foldPostVisible) dynamic.foldPostVisible = false;
      else dynamic.foldPostVisible = true;
    };

    onMounted(() => {
      getLocalStorage();
      pull_aid_dynamic();
    });
    return {
      dynamic,
      store,
      isDynamicLiked,
      GiveLike,
      openPostContent,
    };
  },
};
</script>

<style scoped>
::v-deep .child-input {
  overflow: hidden !important;
  max-height: 0px !important;
  transition: max-height 0.5s ease !important;
}
.fold-post {
  overflow: hidden;
  max-height: 0px;
  transition: max-height 0.5s ease;
  margin-top: 5vh;
}

.expand {
  max-height: 500px;
}

::v-deep .child-expend {
  max-height: 500px !important;
}

.icon-red {
  color: red;
}

.parent-unfold {
  color: lightblue;
  font-size: small;
}
.parent-fold {
  color: lightblue;
  font-size: small;
}
.parent-content {
  margin-left: 1vw;
}
.dynamic-content {
  color: white;
  margin: auto;
  list-style: none;
  overflow: visible;
}

::v-deep .el-divider {
  color: gray !important;
  margin-top: 6px !important;
  margin-bottom: 6px !important;
}
::v-deep .dynamic-content .el-textarea__inner {
  background-color: #2e2e2e !important;
  color: white !important;
}
::v-deep .tab-container .el-textarea__inner {
  background-color: #2e2e2e !important;
  color: white !important;
}
</style>
