<template>
  <ContentField>
    <div class="container">
      <div class="row">
        <div class="col-3">
          <div class="card" style="margin-top: 20px">
            <div class="card-body">
              <img :src="$store.state.user.photo" alt="" style="width: 100%" />
            </div>
          </div>
        </div>
        <div class="col-9">
          <div class="card" style="margin-top: 20px">
            <div class="card-header">
              <span style="font-size: 130%">我的Bot</span>
              <button
                type="button"
                class="btn btn-primary float-end"
                data-bs-toggle="modal"
                data-bs-target="#add-bot-btn"
              >
                创建Bot
              </button>

              <!-- Modal -->
              <div class="modal fade" id="add-bot-btn" tabindex="-1">
                <div class="modal-dialog modal-xl">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title">创建Bot</h5>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="modal-body">
                      <div class="mb-3">
                        <label for="add-bot-title" class="form-label"
                          >名称</label
                        >
                        <input
                          v-model="botadd.title"
                          type="text"
                          class="form-control"
                          id="add-bot-title"
                          placeholder="请输入Bot名称"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="add-bot-description" class="form-label"
                          >简介</label
                        >
                        <textarea
                          v-model="botadd.description"
                          class="form-control"
                          id="add-bot-description"
                          rows="3"
                          placeholder="请输入Bot简介"
                        ></textarea>
                      </div>

                      <div class="mb-3">
                        <label for="add-bot-content" class="form-label"
                          >代码</label
                        >
                        <VAceEditor
                          v-model:value="botadd.content"
                          @init="editorInit"
                          lang="c_cpp"
                          theme="textmate"
                          style="height: 300px"
                        />
                      </div>
                    </div>

                    <div class="modal-footer">
                      <div class="error-message">
                        {{ botadd.error_message }}
                      </div>
                      <button
                        type="button"
                        class="btn btn-primary"
                        @click="add_bot"
                      >
                        创建
                      </button>
                      <button
                        type="button"
                        class="btn btn-secondary"
                        data-bs-dismiss="modal"
                      >
                        取消
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-body">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>Bot名称</th>
                    <th>创建时间</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="bot in bots" :key="bot.id">
                    <td>{{ bot.title }}</td>
                    <td>{{ bot.createtime }}</td>
                    <td>
                      <button
                        type="button"
                        class="btn btn-secondary"
                        style="margin-right: 10px"
                        data-bs-toggle="modal"
                        :data-bs-target="'#update-bot-modal-' + bot.id"
                      >
                        修改
                      </button>
                      <button
                        type="button"
                        class="btn btn-danger"
                        data-bs-toggle="modal"
                        :data-bs-target="'#remove-bot-modal-' + bot.id"
                      >
                        删除
                      </button>

                      <!-- Modal -->
                      <div
                        class="modal fade"
                        :id="'remove-bot-modal-' + bot.id"
                        tabindex="-1"
                      >
                        <div class="modal-dialog">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title">删除Bot</h5>
                              <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                              ></button>
                            </div>
                            <div class="modal-body">
                              <div class="mb-3">
                                <p>你确定要删除该Bot吗？</p>
                              </div>
                            </div>

                            <div class="modal-footer">
                              <button
                                type="button"
                                class="btn btn-primary"
                                @click="remove_bot(bot)"
                              >
                                删除
                              </button>
                              <button
                                type="button"
                                class="btn btn-secondary"
                                data-bs-dismiss="modal"
                              >
                                取消
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>

                      <!-- Modal -->
                      <div
                        class="modal fade"
                        :id="'update-bot-modal-' + bot.id"
                        tabindex="-1"
                      >
                        <div class="modal-dialog modal-xl">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title">修改Bot</h5>
                              <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                              ></button>
                            </div>
                            <div class="modal-body">
                              <div class="mb-3">
                                <label for="add-bot-title" class="form-label"
                                  >名称</label
                                >
                                <input
                                  v-model="bot.title"
                                  type="text"
                                  class="form-control"
                                  id="add-bot-title"
                                  placeholder="请输入Bot名称"
                                />
                              </div>
                              <div class="mb-3">
                                <label
                                  for="add-bot-description"
                                  class="form-label"
                                  >简介</label
                                >
                                <textarea
                                  v-model="bot.description"
                                  class="form-control"
                                  id="add-bot-description"
                                  rows="3"
                                  placeholder="请输入Bot简介"
                                ></textarea>
                              </div>

                              <div class="mb-3">
                                <label for="add-bot-content" class="form-label"
                                  >代码</label
                                >
                                <VAceEditor
                                  v-model:value="bot.content"
                                  @init="editorInit"
                                  lang="c_cpp"
                                  theme="textmate"
                                  style="height: 300px"
                                />
                              </div>
                            </div>

                            <div class="modal-footer">
                              <div class="error-message">
                                {{ bot.error_message }}
                              </div>
                              <button
                                type="button"
                                class="btn btn-primary"
                                @click="update_bot(bot)"
                              >
                                保存修改
                              </button>
                              <button
                                type="button"
                                class="btn btn-secondary"
                                data-bs-dismiss="modal"
                              >
                                取消
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import { ref, reactive } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
// 引入编辑器
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import ContentField from "@/components/ContentField.vue";
export default {
  components: {
    VAceEditor,
    ContentField,
  },
  setup() {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/"
    );

    const store = useStore();
    let bots = ref([]);

    const botadd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });

    const refresh_bots = () => {
      $.ajax({
        url: "https://app4069.acapp.acwing.com.cn:2706/api/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
        },
      });
    };
    refresh_bots();

    const add_bot = () => {
      botadd.error_message = "";
      $.ajax({
        url: "https://app4069.acapp.acwing.com.cn:2706/api/user/bot/add/",
        type: "post",
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            // 将浮窗内容清空，防止下次创建时浮窗内容不为空
            botadd.title = "";
            botadd.description = "";
            botadd.content = "";
            Modal.getInstance("#add-bot-btn").hide(); // 将浮窗隐藏
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message;
          }
        },
      });
    };

    const remove_bot = (bot) => {
      $.ajax({
        url: "https://app4069.acapp.acwing.com.cn:2706/api/user/bot/remove/",
        type: "post",
        data: {
          bot_id: bot.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            Modal.getInstance("#remove-bot-modal-" + bot.id).hide(); // 将浮窗隐藏
            refresh_bots();
          }
        },
      });
    };

    const update_bot = (bot) => {
      botadd.error_message = "";
      $.ajax({
        url: "https://app4069.acapp.acwing.com.cn:2706/api/user/bot/update/",
        type: "post",
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            Modal.getInstance("#update-bot-modal-" + bot.id).hide(); // 将浮窗隐藏
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message;
          }
        },
      });
    };

    return {
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
    };
  },
};
</script>

<style scoped>
div.error-message {
  color: red;
}
</style>
