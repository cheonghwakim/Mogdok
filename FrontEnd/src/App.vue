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
#app {
   width: 100vw;
   height: 100vh;

   overflow: hidden;

   display: flex;
   flex-direction: column;
}
</style>
