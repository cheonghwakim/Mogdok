<template lang="">
   <div class="color">
      <p>{{ colorName }}</p>
      <p>{{ colorCode }}</p>
   </div>
</template>
<script>
export default {
   props: {
      color: String,
   },
   data() {
      return {
         colorName: '-',
         colorCode: '-',
      };
   },
   watch: {
      color: function() {
         switch (this.color) {
            case 'Black':
               this.colorName = 'Black';
               this.colorCode = '#000000';
               break;

            default:
               break;
         }
      },
   },
};
</script>
<style scoped>
.color {
   width: 80vw;
   height: 200px;
   background-color: #000000;

   display: flex;
   justify-content: space-between;

   padding: 0px 30px;

   color: white;
   font-size: 20pt;
}
</style>
