<template>
  <el-row>
    <el-col :span="20"></el-col>
    <el-col :span="4">
      <el-button
        type="info"
        plain
        style="margin-top: 1vh"
        @click="changeNoticeStatus(-1, 0)"
        >全部已读</el-button
      >
    </el-col>
  </el-row>

  <ContentField
    style="padding: 0; border: 1px gray; cursor: pointer"
    v-for="notice in notices"
    :key="notice.notice.id"
    @click="
      GoToAidDynamic(
        notice.notice.dynamicId,
        notice.notice.id,
        notice.notice.status
      )
    "
    :class="{ unread: notice.notice.status == 0 }"
  >
    <el-row>
      <el-col :span="2">
        <el-avatar
          v-if="notice.notice.senderDynamicId == -1"
          :src="notice.senderUser.photo"
        ></el-avatar>
        <el-avatar v-else :src="notice.senderDynamic.userPhoto"> </el-avatar>
      </el-col>
      <el-col :span="17">
        <span
          v-if="notice.notice.senderDynamicId == -1"
          class="notice-senderName"
          >{{ notice.senderUser.username }}</span
        >
        <span class="notice-senderName" v-else>{{
          notice.senderDynamic.username
        }}</span>
        <span style="color: gray; margin-left: 2vw">{{
          notice.notice.createtime
        }}</span>
        <div
          v-if="notice.notice.senderDynamicId == -1"
          style="color: lightblue; font-size: 14px"
        >
          <svg
            class="bi bi-heart"
            width="1em"
            height="1em"
            viewBox="0 0 16 16"
            fill="currentColor"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fill-rule="evenodd"
              d="M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"
            />
          </svg>
        </div>
        <div v-else style="font-size: 16px; overflow: hidden">
          {{ notice.senderDynamic.content }}
        </div>
      </el-col>
      <el-col :span="5">
        <div
          style="
            float: right;
            background-color: #333333;
            overflow: hidden;
            height: 50px;
            width: 50px;
            font-size: 12px;
          "
        >
          {{ notice.userDynamic.content }}
        </div>
      </el-col>
    </el-row>
  </ContentField>
  <el-pagination
    style="float: right; margin-top: 1vh"
    background
    layout="prev, pager, next"
    :pager-count="6"
    :page-size="10"
    :total="noticeCount"
    v-model:current-page="currentPage"
    @current-change="pull_notices"
  />
</template>

<script scoped>
import { onMounted, ref, watch } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import ContentField from "./ContentField.vue";
import $ from "jquery";
import { useRouter } from "vue-router";
export default {
  components: {
    ContentField,
  },
  setup() {
    const notices = ref([]);
    const router = useRouter();
    const store = useStore();
    let currentPage = ref(1);
    let noticeCount = ref(0);
    const pull_notices = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/notice/dynamic/getlist/",
        type: "get",
        data: {
          userId: store.state.user.id,
          page: currentPage.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            notices.value = resp.dynamicNotices;
            noticeCount.value = resp.noticeCount;
          }
        },
      });
    };

    const changeNoticeStatus = (noticeId, status) => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/notice/dynamic/status/change/",
        type: "post",
        data: {
          noticeId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            if (noticeId == -1) {
              store.state.user.noticeCount = 0;
              ElMessage({
                showClose: true,
                message: "已全部已读~",
                type: "success",
              });
              currentPage.value = 1;
              pull_notices();
            } else {
              if (status == 0) store.state.user.noticeCount--;
            }
          }
        },
      });
    };

    const GoToAidDynamic = (dynamicId, noticeId, status) => {
      changeNoticeStatus(noticeId, status);
      router.push({
        name: "aid_dynamic",
        params: {
          dynamicId: dynamicId,
        },
      });
    };

    onMounted(() => {
      if (store.state.user.id != "") {
        pull_notices();
      } else {
        watch(
          () => store.state.user.id,
          () => {
            pull_notices();
          }
        );
      }
    });
    return {
      notices,
      noticeCount,
      currentPage,
      pull_notices,
      changeNoticeStatus,
      GoToAidDynamic,
    };
  },
};
</script>

<style scoped>
.notice-senderName {
  font-size: 14px;
  color: lightblue;
}

.unread {
  border: 1px solid white !important;
}
</style>
