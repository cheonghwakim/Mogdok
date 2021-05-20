<template>
   <div id="app" ref="root">
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
   mounted() {
      window.addEventListener('resize', this.resizeBrowser);
   },
   beforeDestroy() {
      window.removeEventListener('resize', this.resizeBrowser);
   },
   methods: {
      resizeBrowser: function() {
         let vh = window.innerHeight * 0.01;
         // console.log(vh);

         this.$refs.root.style.setProperty('--vh', `${vh}px`);
      },
   },
};
</script>

<style lang="scss">
@import 'src/assets/css/common';
@import 'src/assets/css/moveable';

#app {
   width: 100vw;
   height: 100vh;
   height: calc(var(--vh, 1vh) * 100);

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

   .typicalWrapper::after {
      content: '|';
      animation: blink 1s infinite step-start;
   }

   @keyframes blink {
      50% {
         opacity: 0;
      }
   }
}
</style>
