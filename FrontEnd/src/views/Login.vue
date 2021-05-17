<template lang="">
   <div class="login">
      <div class="lg-container">
         <div class="content">
            <p class="sub">세상에서 가장 귀여운 온라인 독서실</p>
            <p class="main kyoboHand">몽실이네 독서실</p>
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

   .lg-container {
      display: inherit;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 70vw;
      height: 90vh;

      /* border: 1px solid red; */

      .content {
         margin-top: 20vh;

         p.sub {
            color: rgb(164, 164, 164);
            font-weight: 600;
            font-size: 8pt;
            letter-spacing: 3px;
            text-align: center;
         }

         p.main {
            color: rgb(53, 53, 53);
            font-weight: 600;
            font-size: 20pt;
            text-align: center;
            margin-top: 12px;
            margin-bottom: 20px;
         }

         .kakao {
            margin: 0px auto;
            margin-top: 20px;
         }
      }
   }
}
</style>
