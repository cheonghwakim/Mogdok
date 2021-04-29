<template lang="">
   <div class="kakao" @click="doKakaoLogin">
      카카오 로그인
   </div>
</template>
<script>
export default {
   components: {},
   props: {},
   data() {
      return {};
   },
   computed: {},
   watch: {},
   //lifecycle area
   methods: {
      doKakaoLogin() {
         this.$router.push({ name: 'Enterance' });
      },
   },
};
</script>
<style scoped lang="scss">
.kakao {
   width: 200px;
   height: 50px;
   background-color: yellow;

   text-align: center;
   font-size: 20px;
   line-height: 50px;
   color: gray;
   font-weight: 600;

   cursor: pointer;
}
</style>
