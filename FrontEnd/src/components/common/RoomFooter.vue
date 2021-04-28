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
   left: 50%;
   transform: translate(-50%, 20%);
   /* background-color: rgb(173, 199, 254); */
   /* height: $footerHeight; */

   width: 50vw;
   max-width: 600px;
   min-width: 375px;

   height: auto;

   //flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅
   .img-wrapper {
      img {
         width: 100%;
         height: auto;

         object-fit: cover;
         object-position: 0px 0px;
      }
   }
}

@include tinyMobile {
   .footer {
      border: 1px solid red;
      min-width: 100vw;
      min-height: $footerHeight;
      /* transform: scale(1.1); */

      .img-wrapper {
         height: 190px;
         img {
            height: 100%;
            width: auto;

            object-fit: cover;
            object-position: 50% 0px;
         }
      }
   }
}
</style>
