<template lang="">
   <div class="login">
      <div class="container">
         <div class="content">
            <v-lottie-player name="scooterAnim" :width="'250px'" :height="'250px'" loop path="https://assets7.lottiefiles.com/private_files/lf30_z8mfdyic.json" />
            <login-kakao-button></login-kakao-button>
         </div>
         <logo></logo>
      </div>
   </div>
</template>
<script>
import LoginKakaoButton from '@/components/LoginKakaoButton';
import Logo from '@/components/ui/Logo';

export default {
   name: 'Login',
   components: { LoginKakaoButton, Logo },
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
            margin: 0px auto;
            margin-top: 20px;
         }
      }
   }
}
</style>
