<template>
   <div id="app">
      <router-view name="header" />
      <router-view />
      <router-view name="footer" />
   </div>
</template>

<script>
export default {
   name: 'App',
   components: {},
   props: {},
   data() {
      return {};
   },
   computed: {},
   watch: {},
   //lifecycle area
   methods: {},
};
</script>

<style lang="scss">
@import 'src/assets/css/common';
@import 'src/assets/css/moveable';

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
