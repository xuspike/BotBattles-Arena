<template>
  <ContentField>
    <div>
      <el-radio-group v-model="mode">
        <el-radio-button @click="click_mode('贪吃蛇')" label="贪吃蛇" />
        <el-radio-button @click="click_mode('五子棋')" label="五子棋" />
      </el-radio-group>
    </div>
    <table class="table rwd-table" style="text-align: center">
      <thead>
        <tr>
          <th>蓝色方</th>
          <th>玩家A</th>
          <th>红色方</th>
          <th>玩家B</th>
          <th>对战结果</th>
          <th>对战时间</th>
          <th>录像</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in records" :key="record.record.id">
          <td>
            <img :src="record.a_photo" alt="" class="record-user-photo" />
          </td>
          <td>
            <span class="record-user-username">{{ record.a_username }}</span>
          </td>
          <td>
            <img :src="record.b_photo" alt="" class="record-user-photo" />
          </td>
          <td>
            <span class="record-user-username">{{ record.b_username }}</span>
          </td>
          <td>{{ record.result }}</td>
          <td>{{ record.record.createtime }}</td>
          <td>
            <button
              type="button"
              class="custom-btn btn-1"
              @click="open_aid_record(record.record.id)"
            >
              查看录像
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="...">
      <ul class="pagination" style="float: right">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#">上一页</a>
        </li>
        <li
          :class="'page-item ' + page.is_active"
          v-for="page in pages"
          :key="page.number"
          @click="click_page(page.number)"
        >
          <a class="page-link" href="#">{{ page.number }}</a>
        </li>
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#">下一页</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from "vuex";
import $ from "jquery";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    let records = ref([]);
    let current_page = 1;
    let total_records = 0;
    let pages = ref([]);
    let mode = ref("贪吃蛇");

    const click_mode = (aid_mode) => {
      current_page = 1;
      mode.value = aid_mode;
      pull_page(current_page);
    };

    const click_page = (page) => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_records / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_records / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };

    const pull_page = (page) => {
      current_page = page;
      let url = "http://127.0.0.1:3000/api/record/getlist/";
      if (mode.value === "五子棋")
        url = "http://127.0.0.1:3000/api/gobang_record/getlist/";
      $.ajax({
        url: url,
        type: "get",
        data: {
          page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          records.value = resp.records;
          total_records = resp.records_count;
          update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    // 将字符串转为二位数组
    const stringTo2D = (map) => {
      let g = [];
      for (let i = 0, k = 0; i < 13; i++) {
        let line = [];
        for (let j = 0; j < 14; j++, k++) {
          if (map[k] === "0") line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    };

    const open_aid_record = (recordId) => {
      if (mode.value === "贪吃蛇") open_SnakeRecord_content(recordId);
      else if (mode.value === "五子棋") open_GobangRecord_content(recordId);
    };

    const open_SnakeRecord_content = (recordId) => {
      for (const record of records.value) {
        if (record.record.id === recordId) {
          store.commit("updateIsRecord", true);
          store.commit("updateMode", "snake");
          store.commit("updateGame", {
            map: stringTo2D(record.record.map),
            a_id: record.record.aid,
            a_sx: record.record.asx,
            a_sy: record.record.asy,
            b_id: record.record.bid,
            b_sx: record.record.bsx,
            b_sy: record.record.bsy,
          });
          store.commit("updateSteps", {
            a_steps: record.record.asteps,
            b_steps: record.record.bsteps,
          });
          store.commit("updateRecordLoser", record.record.loser);
          router.push({
            name: "record_content",
            params: {
              recordId,
            },
          });
          break;
        }
      }
    };

    const open_GobangRecord_content = (recordId) => {
      for (const record of records.value) {
        if (record.record.id === recordId) {
          store.commit("updateIsRecord", true);
          store.commit("updateMode", "gobang");
          store.commit("updateGame", {
            a_id: record.record.aid,
            b_id: record.record.bid,
          });
          store.commit("updateSteps", {
            a_steps: record.record.asteps,
            b_steps: record.record.bsteps,
          });
          store.commit("updateRecordLoser", record.record.loser);
          store.commit("updateWinnerDirection", record.record.winnerDirection);
          router.push({
            name: "record_content",
            params: {
              recordId,
            },
          });
          break;
        }
      }
    };

    onMounted(() => {
      pull_page(current_page);
    });

    return {
      records,
      pages,
      mode,
      open_aid_record,
      click_page,
      click_mode,
    };
  },
};
</script>

<style scoped lang="scss">
img.record-user-photo {
  width: 5vh;
  border-radius: 50%;
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
  background: #34495e;
  color: #fff;
  border-radius: 0.4em;
  overflow: hidden;
  tr {
    border-color: lighten(#34495e, 10%);
  }
  th,
  td {
    margin: 0.5em 1em;
    @media (min-width: $breakpoint-alpha) {
      padding: 1em !important;
    }
    line-height: 24px;
  }
  th,
  td:before {
    color: #dd5;
  }
}

.custom-btn {
  width: 130px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: "Lato", sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, 0.5),
    7px 7px 20px 0px rgba(0, 0, 0, 0.1), 4px 4px 5px 0px rgba(0, 0, 0, 0.1);
  outline: none;
}

/* 1 */
.btn-1 {
  background: rgb(6, 14, 131);
  background: linear-gradient(
    0deg,
    rgba(6, 14, 131, 1) 0%,
    rgba(12, 25, 180, 1) 100%
  );
  border: none;
}
.btn-1:hover {
  background: rgb(0, 3, 255);
  background: linear-gradient(
    0deg,
    rgba(0, 3, 255, 1) 0%,
    rgba(2, 126, 251, 1) 100%
  );
}
</style>
