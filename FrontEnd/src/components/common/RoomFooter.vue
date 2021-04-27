<template lang="">
   <div class="footer floating">
      <div class="img-wrapper">
         <img src="@/assets/img/noteLong.svg" alt="" />
      </div>
   </div>
</template>
<script>
export default {
   name: 'Footer',
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
<style scoped lang="scss">
@import 'src/assets/css/common';

.footer {
   bottom: 0;
   /* background-color: rgb(173, 199, 254); */
   width: 100%;
   height: $footerHeight;

   //flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅
   .img-wrapper {
      width: 50vw;
      height: auto;

      margin: 0 auto;

      img {
         width: 100%;
         height: auto;

         object-fit: cover;
         object-position: 0px 0px;
      }
   }
}
</style>
