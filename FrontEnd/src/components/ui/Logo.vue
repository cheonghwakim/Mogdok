<template lang="">
   <div class="logo">
      <div class="logo-wrapper">
         <svg-mndk-logo></svg-mndk-logo>
         <p>몽실이네 독서실</p>
      </div>
      <div class="about-modal">
         모달입니당
      </div>
   </div>
</template>
<script>
import SvgMndkLogo from '@/components/svg/SvgMndkLogo';
export default {
   components: { SvgMndkLogo },
};
</script>
<style scoped lang="scss">
.logo {
   .logo-wrapper {
      display: flex;
      flex-direction: column;
      align-items: center;

      p {
         color: #d0d0d0;
         font-size: 0.7rem;
         font-weight: 600;
         margin-top: 5px;
      }
   }

   .about-modal {
      position: fixed;
      width: 300px;
      height: 400px;
      background-color: black;
      z-index: 10;

      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
   }
}
</style>
