export default{
  state: {
    status: "game-selecting", // game-selecting表示选择界面，matching表示匹配界面，playing表示对战界面
    mode: "", // snake表示贪吃蛇,gobang表示五子棋,gravity表示重力四子棋
    socket: null, // 前端和后端建立的连接
    opponent_username: "",
    opponent_photo: "",
    gamemap: null,
    a_id: 0,
    a_sx: 0, // 玩家A贪吃蛇起始横坐标
    a_sy: 0,
    b_id: 0,
    b_sx: 0,  // 玩家B贪吃蛇起始横坐标
    b_sy: 0,
    gameObject: null,
    loser: "none", // none、all、A、B
    winner_direction: -1, // 五子棋连续五子的方向
  },
  getters: {
  },
  mutations: {
    updateSocket(state, socket) {
        state.socket = socket;
    },
    updateOpponent(state, opponent) {
        state.opponent_username = opponent.username;
        state.opponent_photo = opponent.photo;
    },
    updateStatus(state, status) {
        state.status = status;
    },
    updateIsRobot(state, resp) {
      state.is_robot = resp;
    },
    updateMode(state, mode) {
      state.mode = mode;
    },
    updateGame(state, game) {
        state.gamemap = game.map;
        state.a_id = game.a_id;
        state.b_id = game.b_id;
        if(this.mode === "snake") {
          state.a_sx = game.a_sx;
          state.a_sy = game.a_sy;
          state.b_sx = game.b_sx;
          state.b_sy = game.b_sy;
        }
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject;
    },
    updateLoser(state, loser) {
      state.loser = loser;
    },
    updateWinnerDirection(state, winner_direction) { 
      state.winner_direction = winner_direction;
    }
  },
  actions: {
  },
  modules: {
  }
}