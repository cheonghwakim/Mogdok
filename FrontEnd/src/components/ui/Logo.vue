<template lang="">
   <div class="logo">
      <svg-mndk-logo></svg-mndk-logo>
      <p>몽실이네 독서실</p>
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
</style>
