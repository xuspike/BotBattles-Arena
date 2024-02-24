<template>
  <div
    class="dynamic-content"
    v-infinite-scroll="load"
    :infinite-scroll-disabled="disabled"
    infinite-scroll-distance="1"
  >
    <ContentField style="width: 60%">
      <div class="tab-container">
        <el-tabs type="border-card">
          <el-tab-pane label="全部">
            <el-row>
              <el-col :span="2">
                <el-avatar :size="50" :src="store.state.user.photo" />
              </el-col>
              <el-col :span="20">
                <el-input
                  v-model="textarea"
                  :autosize="{ minRows: 2, maxRows: 10 }"
                  type="textarea"
                  placeholder="畅所欲言吧！"
                />
              </el-col>
              <el-col :span="2">
                <el-button
                  type="info"
                  round
                  style="margin-top: 3.5vh; margin-left: 0.8vw"
                  @click="create_dynamic(null, -1)"
                  >发布</el-button
                >
              </el-col>
            </el-row>
            <!-- <div v-if="is_upload_visiable == true"></div> -->
            <div class="demo-collapse">
              <el-collapse v-model="activeNames" @change="handleChange">
                <el-collapse-item name="1">
                  <template #title>
                    <el-row style="color: white">
                      <el-col :span="22">添加照片</el-col>
                      <el-col :span="2"
                        ><el-icon><Picture /></el-icon
                      ></el-col>
                    </el-row>
                  </template>
                  <el-upload
                    list-type="picture-card"
                    :http-request="uploadPhoto"
                    :on-remove="handleRemove"
                    :on-preview="handleReshow"
                    :before-upload="beforePhotoUpload"
                    :limit="6"
                  >
                    <el-icon><Plus /></el-icon>
                    <template #tip>
                      <div class="el-upload__tip">
                        大小不超过2M,图片不超过6张
                      </div>
                    </template>
                  </el-upload>

                  <el-dialog v-model="dialogVisible">
                    <img
                      w-full
                      :src="dialogImageUrl"
                      style="max-width: 100%"
                      alt=""
                    />
                  </el-dialog>
                </el-collapse-item>
              </el-collapse>
            </div>
          </el-tab-pane>
          <el-tab-pane label="好友"
            ><el-row>
              <el-col :span="2">
                <el-avatar :size="50" :src="store.state.user.photo" />
              </el-col>
              <el-col :span="20">
                <el-input
                  v-model="textarea"
                  :autosize="{ minRows: 2, maxRows: 5 }"
                  type="textarea"
                  placeholder="畅所欲言吧！"
                />
              </el-col>
              <el-col :span="2">
                <el-button
                  type="info"
                  round
                  style="margin-top: 3.5vh; margin-left: 0.8vw"
                  >发布</el-button
                >
              </el-col> </el-row
            ><!-- <div v-if="is_upload_visiable == true"></div> -->
            <div class="demo-collapse">
              <el-collapse v-model="activeNames" @change="handleChange">
                <el-collapse-item name="1">
                  <template #title>
                    <el-row style="color: white">
                      <el-col :span="22">添加照片</el-col>
                      <el-col :span="2"
                        ><el-icon><Picture /></el-icon
                      ></el-col>
                    </el-row>
                  </template>
                  <el-upload
                    list-type="picture-card"
                    :http-request="uploadPhoto"
                    :on-remove="handleRemove"
                    :on-preview="handleReshow"
                    :before-upload="beforePhotoUpload"
                    :limit="6"
                  >
                    <el-icon><Plus /></el-icon>
                    <template #tip>
                      <div class="el-upload__tip">
                        大小不超过2M,图片不超过6张
                      </div>
                    </template>
                  </el-upload>

                  <el-dialog v-model="dialogVisible">
                    <img
                      w-full
                      :src="dialogImageUrl"
                      style="max-width: 100%"
                      alt=""
                    />
                  </el-dialog>
                </el-collapse-item>
              </el-collapse></div
          ></el-tab-pane>
        </el-tabs>
      </div>
    </ContentField>
    <ContentField
      v-for="dynamic in dynamics"
      :key="dynamic.parent.id"
      style="width: 60%; color: white"
    >
      <el-dialog
        v-model="dynamic.parent.centerDialogVisible"
        title="Warning"
        width="30%"
        align-center
      >
        <span>您是否要删除该动态？</span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dynamic.parent.centerDialogVisible = false"
              >取消</el-button
            >
            <el-button type="danger" @click="deleteDynamic(dynamic.parent.id)">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>

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
        <el-col
          :span="18"
          style="text-align: right"
          v-if="store.state.user.id == dynamic.parent.userId"
          ><el-icon
            color="red"
            style="cursor: pointer"
            @click="dynamic.parent.centerDialogVisible = true"
            ><Delete /></el-icon
        ></el-col>
      </el-row>
      <el-row>
        <el-col :span="2"></el-col>
        <el-col :span="22">
          <pre
            class="parent-content"
            :id="'parent-content' + dynamic.parent.id"
            v-html="dynamic.parent.content"
            style="
              line-height: 1;
              transition: max-height 0.5s ease;
              max-height: 100px;
              overflow: hidden;
              white-space: pre-line;
            "
          ></pre>
          <span
            v-for="item in dynamic.parent.new_photos.length"
            :key="item"
            style="margin-right: 4px"
          >
            <el-image
              style="width: 100px; height: 100px"
              :src="dynamic.parent.new_photos[item - 1]"
              :zoom-rate="1.2"
              :preview-src-list="dynamic.parent.new_photos"
              :initial-index="item - 1"
              fit="cover"
            />
          </span>
        </el-col>
      </el-row>
      <div
        v-if="
          dynamic.parent.content.length > 100 &&
          judge_is_overflow(dynamic.parent.id)
        "
      >
        <a
          class="parent-unfold"
          v-if="dynamic.is_unfold"
          @click="change_is_unfold(dynamic)"
          style="cursor: pointer; text-decoration: none"
        >
          展开
        </a>
        <a
          class="parent-fold"
          v-else
          @click="change_is_unfold(dynamic)"
          style="cursor: pointer; text-decoration: none"
        >
          收起
        </a>
      </div>
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
          <el-dialog
            v-model="child.centerDialogVisible"
            title="Warning"
            width="30%"
            align-center
          >
            <span>您是否要删除该动态？</span>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="child.centerDialogVisible = false"
                  >取消</el-button
                >
                <el-button type="danger" @click="deleteDynamic(child.id)">
                  确认
                </el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </div>
    </ContentField>
    <div
      v-if="loading"
      style="
        width: 60%;
        background-color: #2e2e2e;
        margin: auto;
        margin-top: 5vh;
      "
      v-loading="loading"
    ></div>
    <ContentField
      v-if="disabled"
      style="width: 60%; background-color: #2e2e2e; text-align: center"
      >到底了~</ContentField
    >
  </div>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import $ from "jquery";
import MarkdownIt from "markdown-it";
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
//import { useRouter } from "vue-router";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
export default {
  components: {
    ContentField,
    Plus,
  },
  setup() {
    const textarea = ref("");
    const store = useStore();
    //const router = useRouter();
    const dynamics = ref([]);
    const is_upload_visiable = ref(false);
    const loading = ref(false);
    const disabled = ref(false);
    const is_get_content = ref(false); // 判断前端是否已经渲染完，从而正常获取id
    let current_page = 0;
    let is_liked = [];
    let photo_list = ref([]);

    // 用来存储每个dynamicId在dynamics中的位置
    let hashmap = new Map();
    let status = new Map();

    const dialogImageUrl = ref(null);
    const dialogVisible = ref(false);

    const handleRemove = (file) => {
      photo_list.value.pop(file.response);
    };

    const handleReshow = (file) => {
      dialogImageUrl.value = file.url;
      dialogVisible.value = true;
    };

    const beforePhotoUpload = (rawFile) => {
      if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
        ElMessage.error("picture must be JPG/PNG format!");
        return false;
      } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error("picture size can not exceed 2MB!");
        return false;
      }
      return true;
    };

    const handlePictureCardPreview = (uploadFile) => {
      dialogImageUrl.value = uploadFile.url;
      dialogVisible.value = true;
    };

    const load = () => {
      if (!loading.value && !disabled.value) {
        loading.value = true;
        pull_dynamics();
      }
    };

    const create_dynamic = (dynamic, reply_id) => {
      let content = "";
      if (dynamic != null) content = dynamic.textarea;
      else content = textarea.value;
      if (content === "") {
        ElMessage({
          showClose: true,
          message: "动态不可为空哦~",
          type: "warning",
        });
        return;
      }
      let photos = "";
      let flag = false;
      for (let i = 0; i < photo_list.value.length; i++) {
        if (!flag) flag = true;
        else photos += "%";
        photos += photo_list.value[i];
      }

      $.ajax({
        url: "https://app6102.acapp.acwing.com.cn/api/dynamic/create/",
        type: "post",
        data: {
          userId: store.state.user.id,
          replyId: reply_id,
          content: content,
          photos: photos,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            ElMessage({
              showClose: true,
              message: "发布成功！",
              type: "success",
            });
          }
          current_page = 0;
          dynamics.value = [];
          pull_dynamics();
          // router.go(0);
        },
      });
    };

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

    const pull_dynamics = () => {
      current_page += 1;
      $.ajax({
        url: "https://app6102.acapp.acwing.com.cn/api/dynamic/getlist/",
        type: "get",
        data: {
          page: current_page,
          userId: "-1",
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          const md = new MarkdownIt();
          if (resp.result === "success") {
            let pastTime = "";
            for (let i = 0; i < resp.dynamics.length; i++) {
              pastTime = change_date(resp.dynamics[i].parent.createtime);
              resp.dynamics[i].parent.pastTime = pastTime;
              resp.dynamics[i].parent.content = md.render(
                resp.dynamics[i].parent.content
              );
              resp.dynamics[i].parent.centerDialogVisible = false;
              resp.dynamics[i].foldPostVisible = false;
              resp.dynamics[i].textarea = "";
              resp.dynamics[i].parent.isIconClicked = false;
              resp.dynamics[i].parent.new_photos =
                resp.dynamics[i].parent.photos.split("%");
              if (is_liked.includes(resp.dynamics[i].parent.id))
                resp.dynamics[i].parent.isIconClicked = true;
              resp.dynamics[i].is_unfold = true;
              for (let j = 0; j < resp.dynamics[i].children.length; j++) {
                pastTime = change_date(resp.dynamics[i].children[j].createtime);
                resp.dynamics[i].children[j].pastTime = pastTime;
                resp.dynamics[i].children[j].content = md.render(
                  resp.dynamics[i].children[j].content
                );
                resp.dynamics[i].children[j].textarea = "";
                resp.dynamics[i].children[j].foldPostVisible = false;
                resp.dynamics[i].children[j].centerDialogVisible = false;
                resp.dynamics[i].children[j].isIconClicked = false;
                if (is_liked.includes(resp.dynamics[i].children[j].id))
                  resp.dynamics[i].children[j].isIconClicked = true;
              }
            }
            for (let j = 0; j < resp.dynamics.length; j++) {
              hashmap.set(
                resp.dynamics[j].parent.id,
                j + dynamics.value.length
              );
            }
            dynamics.value = dynamics.value.concat(resp.dynamics);
            if (resp.parentCnt / 5 <= current_page) {
              disabled.value = true;
            }
            loading.value = false;
            is_get_content.value = false;
          }
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    const deleteDynamic = (dynamicId) => {
      $.ajax({
        url: "https://app6102.acapp.acwing.com.cn/api/dynamic/delete/",
        type: "get",
        data: {
          dynamicId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            ElMessage({
              showClose: true,
              message: "刪除成功！",
              type: "success",
            });
            current_page = 0;
            dynamics.value = [];
            pull_dynamics();
          }
        },
      });
    };

    const get_parentContent = (id) => {
      status.set(id, true);
      return "parent-content" + id;
    };

    // 展开与收起
    const change_is_unfold = (dynamic) => {
      let div_content = document.getElementById(
        "parent-content" + dynamic.parent.id
      );
      if (dynamic.is_unfold) {
        dynamic.is_unfold = false;
        div_content.style.maxHeight = div_content.scrollHeight + "px"; // 展开到文字高度
      } else {
        dynamic.is_unfold = true;
        div_content.style.maxHeight = "100px";
      }
    };

    const judge_is_overflow = (dynamicId) => {
      let parent_content = document.getElementById(
        "parent-content" + dynamicId
      );
      if (parent_content) {
        if (parent_content.scrollHeight > 100) {
          return true;
        } else {
          return false;
        }
      }
      return true;
    };

    // 点赞或者取消点赞, 待完成
    const post_like = (dynamic, num) => {
      console.log(num);
      $.ajax({
        url: "https://app6102.acapp.acwing.com.cn/api/dynamic/give/like/",
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

    const uploadPhoto = (data) => {
      let file = data.file;
      store.dispatch("uploadImage", {
        file,
        success(imgUrl) {
          ElMessage.success("图片上传成功！");
          photo_list.value.push(imgUrl);
          //+ "?x-oss-process=image/resize,h_500,m_lfit";
        },
      });
    };

    onMounted(() => {
      getLocalStorage();
    });

    return {
      current_page,
      dynamics,
      textarea,
      store,
      dialogImageUrl,
      dialogVisible,
      loading,
      disabled,
      is_upload_visiable,
      beforePhotoUpload,
      photo_list,
      handleReshow,
      handleRemove,
      handlePictureCardPreview,
      create_dynamic,
      pull_dynamics,
      // add_dynamics,
      change_is_unfold,
      get_parentContent,
      deleteDynamic,
      load,
      GiveLike,
      isDynamicLiked,
      openPostContent,
      judge_is_overflow,
      uploadPhoto,
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
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
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

::v-deep .tab-container .el-tabs {
  background-color: #2e2e2e;
  border: none !important;
}

::v-deep .tab-container .el-tabs__header {
  background-color: #2e2e2e !important;
  border-bottom: 1px solid grey !important;
}

::v-deep .tab-container .el-textarea__inner {
  background-color: #2e2e2e !important;
  color: white !important;
}

::v-deep .tab-container .el-tabs__item {
  background-color: #2e2e2e !important;
  border-bottom: 1px solid grey !important;
}

::v-deep .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active {
  background-color: darkgray !important;
  color: black !important;
  border-bottom: 1px solid grey !important;
}
::v-deep .tab-container .el-tabs__nav-scroll {
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-collapse-item__header {
  background-color: #2e2e2e !important;
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-collapse-item__content {
  background-color: #2e2e2e !important;
}
</style>
