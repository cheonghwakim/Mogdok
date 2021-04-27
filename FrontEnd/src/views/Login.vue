<template lang="">
   <div class="login">
      <div class="container">
         <div class="content">
            <mongsil></mongsil>
            <login-kakao></login-kakao>
         </div>
         <logo></logo>
      </div>
   </div>
</template>
<script>
import LoginKakao from '@/components/LoginKakao';
import Logo from '@/components/ui/Logo';
import Mongsil from '@/components/svg/Mongsil';

export default {
   name: 'Login',
   components: { LoginKakao, Mongsil, Logo },
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
.login {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: center;

   .container {
      display: inherit;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 70vw;
      height: 90vh;

      /* border: 1px solid red; */

      .content {
         margin-top: 20vh;

         .kakao {
            margin-top: 20px;
         }
      }
   }
}
</style>
