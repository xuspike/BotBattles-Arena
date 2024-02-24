<template>
  <div class="card-container">
    <img class="round" :src="$store.state.user.photo" alt="user" />
    <h3>{{ $store.state.user.username }}</h3>
    <p>
      {{ $store.state.user.resume }}
    </p>
    <div>
      <el-row style="color: #81878b">
        <el-col :span="8">
          <div class="title">
            <span>动态数</span>
            <span class="number">12</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="title">
            <span>好友数</span>
            <span class="number">32</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="title">
            <span>获赞数</span>
            <span class="number">55</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
h3 {
  margin: 10px 0;
}

h6 {
  margin: 5px 0;
  text-transform: uppercase;
}

p {
  font-size: 14px;
  line-height: 21px;
}

.card-container {
  background-color: #2e2e2e;
  border-radius: 5px;
  color: #b3b8cd;
  padding-top: 30px;
  position: relative;
  width: 350px;
  max-width: 100%;
  text-align: center;
}

.card-container .round {
  border: 1px solid #03bfcb;
  border-radius: 50%;
  padding: 7px;
  width: 10vw;
}

.title {
  text-align: center;
  display: inline-block;
  font-family: "Lato";
  font-size: 0.8em;
  border-right: solid 1px #bdc3c7;
}

.title:last-child {
  border: none;
}

.number {
  display: block;
  text-transform: uppercase;
  font-family: "Quicksand";
  font-size: 1em;
  margin-top: 0.3em;
  font-weight: 700;
}
</style>
