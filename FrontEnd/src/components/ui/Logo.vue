<template lang="">
   <div class="logo">
      <div class="logo-wrapper" @click="toggleAbout">
         <svg-mndk-logo></svg-mndk-logo>
         <p>몽실이네 독서실</p>
      </div>
      <modal-about :isOpenAbout="isOpenAbout" @onClick="closeModalAbout"></modal-about>
      <!-- <div class="about-modal">
         모달
      </div> -->
   </div>
</template>
<script>
import SvgMndkLogo from '@/components/svg/SvgMndkLogo';
import ModalAbout from '@/components/ui/ModalAbout';

export default {
   components: { SvgMndkLogo, ModalAbout },
   data() {
      return {
         isOpenAbout: false,
      };
   },
   methods: {
      toggleAbout: function() {
         this.isOpenAbout = !this.isOpenAbout;
      },
      closeModalAbout: function() {
         this.isOpenAbout = false;
      },
   },
};
</script>
<style scoped lang="scss">
.logo {
   .logo-wrapper {
      display: flex;
      flex-direction: column;
      align-items: center;

      cursor: pointer;

      p {
         color: #d0d0d0;
         font-size: 0.7rem;
         font-weight: 600;
         margin-top: 5px;
      }
   }
}
</style>
