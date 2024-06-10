<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <WhiteContentField>
          <WhiteProfileCard></WhiteProfileCard>
        </WhiteContentField>
      </div>
      <div class="col-9">
        <el-card shadow="always" class="bot-card" style="user-select: none">
          <template #header>
            <div class="card-header">
              <span style="font-weight: 700; font-size: 18px"> 我的Bot </span>
              <el-button
                type="primary"
                class="button"
                style="float: right"
                size="small"
                plain
                @click="showCreateDialog = true"
                >创造Bot</el-button
              >
              <!-- 创建模态框 -->
              <el-dialog
                style="padding: 20px"
                v-model="showCreateDialog"
                title="创建Bot"
                @opened="openCodeDialog('-create')"
              >
                <el-form
                  label-position="top"
                  label-width="100px"
                  :model="bot"
                  style="max-width: 100%"
                  :rules="rules"
                  ref="createForm"
                >
                  <el-form-item label="名称" prop="title">
                    <el-input
                      v-model="bot.title"
                      placeholder="请输入Bot名称"
                      clearable
                    />
                  </el-form-item>
                  <el-form-item label="游戏" prop="game">
                    <el-radio-group v-model="bot.game">
                      <el-radio-button
                        v-for="game in games"
                        :key="game.id"
                        :label="game.name"
                        >{{ game.name }}</el-radio-button
                      >
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="简介" prop="description">
                    <el-input
                      v-model="bot.description"
                      :autosize="{ minRows: 3 }"
                      type="textarea"
                      placeholder="请输入Bot简介"
                    />
                  </el-form-item>
                  <el-form-item label="语言" prop="language">
                    <el-radio-group v-model="bot.language">
                      <el-radio-button label="cpp">cpp</el-radio-button>
                      <el-radio-button label="java">java</el-radio-button>
                      <el-radio-button label="python">python</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="代码">
                    <div style="flex-grow: 1" />
                    <span class="refresh" @click="refreshEditor">
                      <el-icon :size="25">
                        <Refresh />
                      </el-icon>
                    </span>

                    <el-dropdown trigger="click">
                      <span class="gear">
                        <el-icon :size="25">
                          <Tools />
                        </el-icon>
                      </span>
                      <template #dropdown>
                        <el-dropdown-menu style="user-select: none">
                          <el-form-item
                            label="界面风格"
                            style="margin: 15px 15px"
                          >
                            <el-radio-group
                              v-model="skin"
                              @change="changeEditSkin"
                            >
                              <el-radio-button label="chrome"
                                >chrome</el-radio-button
                              >
                              <el-radio-button label="monokai"
                                >monokai</el-radio-button
                              >
                            </el-radio-group>
                          </el-form-item>
                          <el-form-item
                            label="编辑类型"
                            style="margin: 15px 15px"
                          >
                            <el-radio-group
                              v-model="editor_mode"
                              @change="changeEditMode"
                            >
                              <el-radio-button label="vscode"
                                >Standard</el-radio-button
                              >
                              <el-radio-button label="vim">Vim</el-radio-button>
                              <el-radio-button label="emacs"
                                >Emacs</el-radio-button
                              >
                            </el-radio-group>
                          </el-form-item>
                          <el-form-item
                            label="缩进长度"
                            style="margin: 15px 15px"
                          >
                            <el-radio-group
                              v-model="editor_space"
                              @change="changeEditorSpace"
                            >
                              <el-radio-button label="2">2</el-radio-button>
                              <el-radio-button label="4">4</el-radio-button>
                              <el-radio-button label="8">8</el-radio-button>
                            </el-radio-group>
                          </el-form-item>
                          <el-form-item label="字号" style="margin: 15px 15px">
                            <el-input-number
                              v-model="fontsize"
                              :min="5"
                              :max="60"
                              size="small"
                              controls-position="right"
                              @change="changeEditFont"
                            />
                          </el-form-item>
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>

                    <VAceEditor
                      v-model:value="bot.content"
                      id="codeEditor-create"
                      @init="editorInit"
                      class="codeEditor"
                      lang="c_cpp"
                      theme="textmate"
                      style="height: 300px; width: 100%"
                    />
                  </el-form-item>
                  <el-card class="debug-card" v-if="showDebugPannel">
                    <template #header>
                      <div class="card-header">
                        <div>
                          <span> 代码运行状态： </span>
                          <span
                            :style="
                              codeStatus === 'Finished'
                                ? 'color: rgb(68, 157, 68); font-weight: 600;'
                                : codeStatus === 'Running...'
                                ? 'color: rgb(51, 122, 183); font-weight: 600;'
                                : 'color: rgb(208, 84, 81); font-weight: 600;'
                            "
                          >
                            {{ codeStatus }}
                          </span>
                          <button
                            class="card-close-btn"
                            @click="closeDebugPannel"
                          >
                            x
                          </button>
                        </div>
                      </div>
                    </template>
                    <div class="card-body">
                      <div class="pannel-body">
                        <div>
                          <label
                            for="run-code-stdin"
                            style="font-weight: normal; font-size: 15px"
                            >输入</label
                          >
                          <br />
                          <el-input
                            id="run-code-stdin"
                            type="textarea"
                            class="card-code-input"
                            maxlength="2000"
                            rows="1"
                            resize="none"
                            v-model="codeInput"
                            :autosize="{ minRows: 1 }"
                            style="margin-top: 5px"
                          ></el-input>
                        </div>
                        <div style="margin-top: 5px">
                          <label style="font-weight: normal; font-size: 15px"
                            >输出</label
                          >
                          <br />
                          <el-input
                            id="run-code-stdout"
                            type="textarea"
                            class="card-code-output"
                            disabled
                            maxlength="2000"
                            rows="1"
                            resize="none"
                            v-model="codeOutput"
                            :autosize="{ minRows: 1 }"
                            style="margin-top: 5px"
                          ></el-input>
                        </div>
                        <div
                          style="margin-top: 5px"
                          v-if="codeStatus === 'Finished'"
                        >
                          运行时间：
                          <span>{{ codeTime }}ms</span>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-form>
                <template #footer>
                  <span class="dialog-footer">
                    <el-button
                      type="warning"
                      @click="debugCode(bot.language, bot.content)"
                      :loading="submitCoding"
                      >调试</el-button
                    >
                    <el-button type="primary" @click="confirmCreateBot"
                      >创建</el-button
                    >
                    <el-button @click="cancelCreateBot">取消</el-button>
                  </span>
                </template>
              </el-dialog>
            </div>
          </template>
          <!-- 表格数据 -->
          <el-table
            :data="filterBots"
            stripe
            style="width: 100%"
            highlight-current-row
            border
            max-height="67vh"
            table-layout="auto"
          >
            <el-table-column label="序号" type="index" />
            <el-table-column prop="title" label="名称" />
            <el-table-column prop="game" label="游戏" />
            <el-table-column prop="language" label="语言" />
            <el-table-column prop="createtime" label="创建时间" sortable />

            <el-table-column fixed="right" label="Operations">
              <template #default="scope">
                <el-button
                  size="small"
                  @click="openEditBot(scope.row)"
                  color="#626aef"
                  >修改</el-button
                >
                <el-button
                  size="small"
                  type="danger"
                  @click="openDeleteBot(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>

          <!-- 修改模态框 -->
          <el-dialog
            style="padding: 20px"
            v-model="showEditDialog"
            title="修改Bot"
            @opened="openCodeDialog('-edit')"
          >
            <el-form
              label-position="top"
              label-width="100px"
              :model="currentOpBot"
              style="max-width: 100%"
              :rules="rules"
              ref="editForm"
            >
              <el-form-item label="名称" prop="title">
                <el-input
                  v-model="currentOpBot.title"
                  placeholder="请输入Bot名称"
                  clearable
                />
              </el-form-item>
              <el-form-item label="简介" prop="description">
                <el-input
                  v-model="currentOpBot.description"
                  :autosize="{ minRows: 3 }"
                  type="textarea"
                  placeholder="请输入Bot简介"
                />
              </el-form-item>
              <el-form-item label="语言" prop="language">
                <el-radio-group v-model="currentOpBot.language">
                  <el-radio-button label="cpp">cpp</el-radio-button>
                  <el-radio-button label="java">java</el-radio-button>
                  <el-radio-button label="python">python</el-radio-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="代码">
                <div style="flex-grow: 1" />
                <span class="refresh" @click="refreshEditor">
                  <el-icon :size="25">
                    <Refresh />
                  </el-icon>
                </span>

                <el-dropdown trigger="click">
                  <span class="gear">
                    <el-icon :size="25">
                      <Tools />
                    </el-icon>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu style="user-select: none">
                      <el-form-item label="界面风格" style="margin: 15px 15px">
                        <el-radio-group v-model="skin" @change="changeEditSkin">
                          <el-radio-button label="chrome"
                            >chrome</el-radio-button
                          >
                          <el-radio-button label="monokai"
                            >monokai</el-radio-button
                          >
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="编辑类型" style="margin: 15px 15px">
                        <el-radio-group
                          v-model="editor_mode"
                          @change="changeEditMode"
                        >
                          <el-radio-button label="vscode"
                            >Standard</el-radio-button
                          >
                          <el-radio-button label="vim">Vim</el-radio-button>
                          <el-radio-button label="emacs">Emacs</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="缩进长度" style="margin: 15px 15px">
                        <el-radio-group
                          v-model="editor_space"
                          @change="changeEditorSpace"
                        >
                          <el-radio-button label="2">2</el-radio-button>
                          <el-radio-button label="4">4</el-radio-button>
                          <el-radio-button label="8">8</el-radio-button>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="字号" style="margin: 15px 15px">
                        <el-input-number
                          v-model="fontsize"
                          :min="5"
                          :max="60"
                          size="small"
                          controls-position="right"
                          @change="changeEditFont"
                        />
                      </el-form-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>

                <VAceEditor
                  v-model:value="currentOpBot.content"
                  id="codeEditor-edit"
                  @init="editorInit"
                  class="codeEditor"
                  lang="c_cpp"
                  theme="textmate"
                  style="height: 300px; width: 100%"
                />
              </el-form-item>
              <el-card class="debug-card" v-if="showDebugPannel">
                <template #header>
                  <div class="card-header">
                    <div>
                      <span> 代码运行状态： </span>
                      <span
                        :style="
                          codeStatus === 'Finished'
                            ? 'color: rgb(68, 157, 68); font-weight: 600;'
                            : codeStatus === 'Running...'
                            ? 'color: rgb(51, 122, 183); font-weight: 600;'
                            : 'color: rgb(208, 84, 81); font-weight: 600;'
                        "
                      >
                        {{ codeStatus }}
                      </span>
                      <button class="card-close-btn" @click="closeDebugPannel">
                        x
                      </button>
                    </div>
                  </div>
                </template>
                <div class="card-body">
                  <div class="pannel-body">
                    <div>
                      <label
                        for="run-code-stdin"
                        style="font-weight: normal; font-size: 15px"
                        >输入</label
                      >
                      <br />
                      <el-input
                        id="run-code-stdin"
                        type="textarea"
                        class="card-code-input"
                        maxlength="2000"
                        rows="1"
                        resize="none"
                        v-model="codeInput"
                        :autosize="{ minRows: 1 }"
                        style="margin-top: 5px"
                      ></el-input>
                    </div>
                    <div style="margin-top: 5px">
                      <label style="font-weight: normal; font-size: 15px"
                        >输出</label
                      >
                      <br />
                      <el-input
                        id="run-code-stdout"
                        type="textarea"
                        class="card-code-output"
                        disabled
                        maxlength="2000"
                        rows="1"
                        resize="none"
                        v-model="codeOutput"
                        :autosize="{ minRows: 1 }"
                        style="margin-top: 5px"
                      ></el-input>
                    </div>
                    <div
                      style="margin-top: 5px"
                      v-if="codeStatus === 'Finished'"
                    >
                      运行时间：
                      <span>{{ codeTime }}ms</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button
                  type="warning"
                  @click="
                    debugCode(currentOpBot.language, currentOpBot.content)
                  "
                  :loading="submitCoding"
                >
                  调试
                </el-button>
                <el-button type="primary" @click="confirmEditBot"
                  >保存修改</el-button
                >
                <el-button @click="cancelEditBot">取消</el-button>
              </span>
            </template>
          </el-dialog>

          <!-- 删除模态框 -->
          <el-dialog
            v-model="showDeleteDialog"
            title="删除bot"
            width="30%"
            :before-close="handleClose"
          >
            <span style="padding: 20px">
              确认删除标题为
              <span class="content-ellipsis"> {{ currentOpBot.title }} </span>
              吗?
            </span>
            <template #footer>
              <span class="dialog-footer">
                <el-button type="primary" @click="confirmDeleteBot"
                  >确认删除</el-button
                >
                <el-button @click="cancelDeleteBot">取消</el-button>
              </span>
            </template>
          </el-dialog>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, unref, reactive, computed, onMounted } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
// 引入编辑器
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import WhiteProfileCard from "../../../components/WhiteProfileCard.vue";
import WhiteContentField from "../../../components/WhiteContentField.vue";
import { ElMessage } from "element-plus";
export default {
  components: {
    VAceEditor,
    WhiteProfileCard,
    WhiteContentField,
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
    let games = ref([
      { id: 0, name: "snake" },
      { id: 1, name: "gobang" },
      { id: 2, name: "gravity" },
    ]);
    let search = ref("");
    let showCreateDialog = ref(false);
    let showEditDialog = ref(false);
    let showDeleteDialog = ref(false);
    let createForm = ref(null);
    let editForm = ref(null);
    let currentOpBot = ref(null);
    let editor = null;
    let skin = ref("chrome");
    let fontsize = ref(11);
    let editor_mode = ref("vscode");
    let editor_space = ref(4);
    let submitCoding = ref(false);
    let codeInput = ref("");
    let codeOutput = ref("");
    let showDebugPannel = ref(false);
    let codeStatus = ref("");
    let codeTime = ref(0);
    let canCreateBotCnt = ref(0);
    let isExpand = ref(null);

    const editorInit = (editor) => {
      editor.renderer.setShowPrintMargin(false);
    };

    const initModal = () => {
      if (
        localStorage.getItem("editor_skin_for_" + store.state.user.username) !==
        null
      ) {
        skin.value = localStorage.getItem(
          "editor_skin_for_" + store.state.user.username
        );
      }

      if (
        localStorage.getItem(
          "editor_fontsize_for_" + store.state.user.username
        ) !== null
      ) {
        fontsize.value = parseInt(
          localStorage.getItem(
            "editor_fontsize_for_" + store.state.user.username
          )
        );
      }

      if (
        localStorage.getItem("editor_mode_for_" + store.state.user.username) !==
        null
      ) {
        editor_mode.value = localStorage.getItem(
          "editor_mode_for_" + store.state.user.username
        );
      }

      if (
        localStorage.getItem(
          "editor_space_for_" + store.state.user.username
        ) !== null
      ) {
        editor_space.value = localStorage.getItem(
          "editor_space_for_" + store.state.user.username
        );
      }

      editor.setOptions({
        fontSize: `${fontsize.value}pt`,
        tabSize: parseInt(editor_space.value),
      });
      editor.setKeyboardHandler("ace/keyboard/" + editor_mode.value);
      editor.setTheme("ace/theme/" + skin.value);
    };

    const changeEditSkin = () => {
      editor.setTheme("ace/theme/" + skin.value);
      localStorage.setItem(
        "editor_skin_for_" + store.state.user.username,
        skin.value
      );
    };

    const changeEditFont = (num) => {
      editor.setOptions({
        fontSize: `${num}pt`,
      });
      fontsize.value = num;
      localStorage.setItem(
        "editor_fontsize_for_" + store.state.user.username,
        num
      );
    };

    const changeEditMode = () => {
      editor.setKeyboardHandler("ace/keyboard/" + editor_mode.value);
      localStorage.setItem(
        "editor_mode_for_" + store.state.user.username,
        editor_mode.value
      );
    };

    const changeEditorSpace = () => {
      editor.setOptions({
        tabSize: parseInt(editor_space.value),
      });
      localStorage.setItem(
        "editor_space_for_" + store.state.user.username,
        editor_space.value
      );
    };

    const refreshEditor = () => {
      editor.setValue("");
    };

    const bot = reactive({
      title: "",
      game: "",
      description: "",
      language: "",
      content: "",
    });

    const refresh_bots = () => {
      $.ajax({
        url: "https://xrookie.xyz/api/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
          console.log(bots.value);
        },
        error() {
          console.log("error");
        },
      });
    };

    const add_bot = () => {
      if (
        bot.title == "" ||
        bot.content == "" ||
        bot.game == "" ||
        bot.language == ""
      ) {
        ElMessage.error("请将bot信息填完整！");
        return;
      }
      $.ajax({
        url: "https://xrookie.xyz/api/user/bot/add",
        type: "post",
        data: {
          title: bot.title,
          game: bot.game,
          description: bot.description,
          language: bot.language,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            bot.title = "";
            bot.game = 0;
            bot.description = "";
            bot.content = "";
            refresh_bots();
            ElMessage({
              message: "创建成功",
              type: "success",
            });
          } else {
            ElMessage.error(resp.error_message);
          }
        },
        error() {
          store.dispatch("logout");
        },
      });
    };

    const remove_bot = (bot) => {
      $.ajax({
        url: "https://xrookie.xyz/api/user/bot/remove/",
        type: "post",
        data: {
          bot_id: bot.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            refresh_bots();
            ElMessage({
              showClose: true,
              message: "删除成功",
              type: "success",
            });
          }
        },
        error() {
          store.dispatch("logout");
        },
      });
    };

    const update_bot = (bot) => {
      $.ajax({
        url: "https://xrookie.xyz/api/user/bot/update/",
        type: "post",
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          language: bot.language,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            ElMessage({
              showClose: true,
              message: "更新成功",
              type: "success",
            });
          } else {
            ElMessage.error(resp.error_message);
          }
        },
        error() {
          store.dispatch("logout");
        },
      });
    };

    const filterBots = computed(
      () => bots.value,
      bots.value.filter(
        (bot) =>
          !search.value ||
          bot.game.toLowerCase().includes(search.value.toLowerCase()) ||
          bot.title.toLowerCase().includes(search.value.toLowerCase()) ||
          bot.createtime.includes(search.value)
      )
    );

    const openCodeDialog = (flag) => {
      editor = ace.edit("codeEditor" + flag);
      initModal();
    };

    const openEditBot = (row) => {
      currentOpBot.value = row;
      showEditDialog.value = true;
    };

    const openDeleteBot = (row) => {
      showDeleteDialog.value = true;
      currentOpBot.value = row;
    };

    const confirmCreateBot = async () => {
      const form = unref(createForm);
      console.log(form.value);
      await form.validate((valid) => {
        if (bot.content === "") {
          ElMessage({
            showClose: true,
            message: "代码不能为空",
            type: "error",
          });
          return;
        } else if (bot.content.length > 10000) {
          ElMessage({
            showClose: true,
            message: "代码不能超过10000",
            type: "error",
          });
          return;
        }
        if (valid) {
          showCreateDialog.value = false;
          add_bot();
        }
      });
    };

    const checkVal = (rule, value, callback) => {
      if (bot.game === "") {
        callback(new Error("请选择游戏！"));
      } else {
        callback();
      }
    };

    const cancelCreateBot = () => {
      showCreateDialog.value = false;
      ElMessage("取消创建");
      const form = unref(createForm);
      form.resetFields();
      closeDebugPannel();
    };

    const confirmEditBot = async () => {
      const form = unref(editForm);
      await form.validate((valid) => {
        if (currentOpBot.value.content === "") {
          ElMessage({
            showClose: true,
            message: "代码不能为空",
            type: "error",
          });
          return;
        } else if (currentOpBot.value.content.length > 10000) {
          ElMessage({
            showClose: true,
            message: "代码不能超过10000",
            type: "error",
          });
          return;
        }
        if (valid) {
          showEditDialog.value = false;
          update_bot(currentOpBot.value);
        }
      });
    };

    const cancelEditBot = () => {
      showEditDialog.value = false;
      ElMessage("取消修改");
      const form = unref(editForm);
      form.resetFields();
      closeDebugPannel();
    };

    const confirmDeleteBot = () => {
      showDeleteDialog.value = false;
      remove_bot(currentOpBot.value);
    };

    const cancelDeleteBot = () => {
      showDeleteDialog.value = false;
      ElMessage("取消删除");
    };

    const debugCode = (lang, code) => {
      if (code === "") {
        ElMessage.error("代码为空，无法调试");
        submitCoding.value = false;
        return;
      }
      if (lang === "") {
        ElMessage.error("请选择语言");
        submitCoding.value = false;
        return;
      }
      showDebugPannel.value = true;
      codeOutput.value = "";
      submitCoding.value = true;
      codeStatus.value = "Running...";
      (lang = lang.trim()), (code = code.trim());
      $.ajax({
        url: "https://aigame.zzqahm.top/backend/player/bot/debug/",
        type: "post",
        data: {
          lang: lang,
          code: code,
          data: codeInput.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.access,
        },
        success(resp) {
          codeStatus.value = resp.status;
          codeOutput.value = resp.output;
          submitCoding.value = false;
          if (codeStatus.value === "Finished") {
            codeTime.value = resp.time;
          }
        },
        error() {
          console.log("error");
        },
      });
    };

    const closeDebugPannel = () => {
      showDebugPannel.value = false;
      codeOutput.value = "";
      codeInput.value = "";
      codeTime.value = 0;
    };

    onMounted(() => {
      refresh_bots();
    });
    return {
      bots,
      games,
      bot,
      search,
      filterBots,
      showCreateDialog,
      showEditDialog,
      showDeleteDialog,
      openCodeDialog,
      openEditBot,
      openDeleteBot,
      confirmCreateBot,
      cancelCreateBot,
      confirmEditBot,
      cancelEditBot,
      confirmDeleteBot,
      cancelDeleteBot,
      createForm,
      editForm,
      currentOpBot,
      editorInit,
      initModal,
      changeEditSkin,
      changeEditFont,
      changeEditMode,
      changeEditorSpace,
      skin,
      fontsize,
      editor_mode,
      editor_space,
      refreshEditor,
      debugCode,
      submitCoding,
      codeInput,
      codeOutput,
      showDebugPannel,
      closeDebugPannel,
      codeStatus,
      codeTime,
      isExpand,
      canCreateBotCnt,
      rules: {
        title: [
          { required: true, message: "名称不得为空！", trigger: "blur" },
          { max: 100, message: "长度在 100 个字符之内", trigger: "blur" },
        ],
        game: [
          {
            validator: checkVal,
            required: true,
            message: "请选择游戏！",
            trigger: "blur",
          },
        ],
        description: [
          { max: 300, message: "长度在 300 个字符之内", trigger: "blur" },
        ],
        language: [
          { required: true, message: "请选择语言！", trigger: "blur" },
        ],
      },
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

.bot-card {
  margin-top: 20px;
}

.bot-card .card-header {
  align-items: center;
}

.refresh {
  margin-right: 15px;
  cursor: pointer;
}

.refresh:hover {
  transform: scale(1.2);
  transition: 100ms;
}

.gear {
  cursor: pointer;
  position: relative;
  bottom: 4px;
}

.gear:hover {
  transform: scale(1.2);
  transition: 100ms;
}

.debug-card:deep(.el-card__header) {
  color: #333;
  background-color: #f5f5f5;
  border-color: #ddd;
}

.debug-card .card-close-btn {
  -webkit-appearance: none;
  padding: 0;
  cursor: pointer;
  background: 0 0;
  border: none;
}

.debug-card .card-close-btn {
  float: right;
  font-size: 21px;
  font-weight: 700;
  text-shadow: 0 1px 0 #fff;
  opacity: 0.2;
  line-height: 21px;
}

.debug-card .card-close-btn:hover {
  opacity: 1;
}

.debug-card .pannel-body {
  padding: 7px 60px 10px 35px;
}

.debug-card .card-code-input:deep(.el-textarea__inner),
.debug-card .card-code-output:deep(.el-textarea__inner) {
  background-color: rgb(248, 248, 248);
  font-size: 15px;
  font-family: monospace;
}

.debug-card .card-code-output:deep(.el-textarea__inner) {
  color: black;
  font-size: 12px;
}
.content-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100px;
  display: inline-block;
  position: relative;
  top: 5px;
  text-align: center;
  font-weight: 600;
}

::v-deep.container .card {
  background-color: #ffffff !important;
}
</style>
