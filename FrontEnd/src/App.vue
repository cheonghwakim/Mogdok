<template>
  <div id="app">
    <router-view name="header" />
    <router-view />
    <router-view name="footer" />
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
  props: {},
  data() {
    return {};
  },
  computed: {},
  created() {
    console.log(window.location.hostname );
    console.log(process.env);
    if (process.env.NODE_ENV=="production"&&(window.location.protocol !== "https:" || window.location.hostname !=="www.mongdok.com") ) {//k4a401.p.ssafy.io일경우
      window.location.href = "https://www.mongdok.com";
    }
  },
  watch: {},
  //lifecycle area
  methods: {},
};
</script>

<style lang="scss">
@import "src/assets/css/common";
@import "src/assets/css/moveable";

#app {
  width: 100vw;
  height: 100vh;

  display: flex;
  flex-direction: column;

  // 스크롤바 스타일링
  ::-webkit-scrollbar {
    width: 8px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: rgb(203, 203, 203);
    border-radius: 10px;
  }
  ::-webkit-scrollbar-track {
    background-color: transparent;
    border-radius: 20px;
  }
}
</style>
