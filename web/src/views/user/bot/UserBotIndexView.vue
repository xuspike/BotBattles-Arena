<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <ContentField>
          <ProfileCard></ProfileCard>
        </ContentField>
      </div>
      <div class="col-9">
        <ContentField>
          <span style="font-size: 130%; color: #fff">我的Bot</span>
          <button
            type="button"
            class="btn btn-primary float-end"
            data-bs-toggle="modal"
            data-bs-target="#add-bot-btn"
          >
            创建Bot
          </button>

          <!-- Modal -->
          <div
            class="modal fade"
            id="add-bot-btn"
            tabindex="-1"
            style="color: black"
          >
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
                    <label for="add-bot-title" class="form-label">名称</label>
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
                    <label for="add-bot-content" class="form-label">代码</label>
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
                  <div class="error-message">{{ botadd.error_message }}</div>
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
          <div class="card-body">
            <table class="table rwd-table" style="text-align: center">
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
                      style="color: black"
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
                      style="color: black"
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
        </ContentField>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
// 引入编辑器
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import ProfileCard from "../../../components/ProfileCard.vue";
import ContentField from "../../../components/ContentField.vue";
export default {
  components: {
    VAceEditor,
    ProfileCard,
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
        url: "http://127.0.0.1:3000/api/user/bot/getlist/",
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
        url: "http://127.0.0.1:3000/api/user/bot/add/",
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
        url: "http://127.0.0.1:3000/api/user/bot/remove/",
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
        url: "http://127.0.0.1:3000/api/user/bot/update/",
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

<style scoped lang="scss">
div.error-message {
  color: red;
}

$breakpoint-alpha: 480px; // adjust to your needs

.rwd-table {
  margin: 1em 0;
  min-width: 300px; // adjust to your needs

  tr {
    border-top: 1px solid #ddd;
    border-bottom: 1px solid #ddd;
  }

  th {
    display: none; // for accessibility, use a visually hidden method here instead! Thanks, reddit!
  }

  td {
    display: block;

    &:first-child {
      padding-top: 0.5em;
    }
    &:last-child {
      padding-bottom: 0.5em;
    }

    &:before {
      content: attr(data-th) ": "; // who knew you could do this? The internet, that's who.
      font-weight: bold;

      // optional stuff to make it look nicer
      width: 6.5em; // magic number :( adjust according to your own content
      display: inline-block;
      // end options

      @media (min-width: $breakpoint-alpha) {
        display: none;
      }
    }
  }

  th,
  td {
    text-align: left;

    @media (min-width: $breakpoint-alpha) {
      display: table-cell;
      padding: 0.25em 0.5em;

      &:first-child {
        padding-left: 0;
      }

      &:last-child {
        padding-right: 0;
      }
    }
  }
}

// presentational styling

@import "https://fonts.googleapis.com/css?family=Montserrat:300,400,700";

h1 {
  font-weight: normal;
  letter-spacing: -1px;
  color: #34495e;
}

.rwd-table {
  background: #2e2e2e;
  color: #fff;
  border-radius: 0.4em;
  overflow: hidden;
  tr {
    border-color: lighten(#2e2e2e, 10%);
  }
  th,
  td {
    margin: 0.5em 1em;
    @media (min-width: $breakpoint-alpha) {
      padding: 1em !important;
    }
  }
  th,
  td:before {
    color: #dd5;
  }
}
</style>
