<template lang="">
   <!-- 사용하는 곳에선, 사이즈와 위치만 지정하면 된다. -->
   <div v-wave class="btn-rounded" @click="onClick" :class="[{ floating: type == 'floating' }, setColor]">
      {{ label }}
   </div>
</template>
<script>
export default {
   props: {
      label: String,
      type: String,
      color: String,
   },

   methods: {
      onClick: function() {
         this.$emit('onClick');
      },
   },

   computed: {
      setColor() {
         if (this.color) {
            return this.color;
         } else {
            return 'green';
         }
      },
   },
};
</script>
<style scoped lang="scss">
.btn-rounded {
   width: 100%;
   height: 45px;

   font-size: 10pt;
   text-transform: uppercase;
   letter-spacing: 2.5px;
   text-align: center;
   line-height: 45px;

   font-weight: 600;
   color: rgb(63, 63, 63);
   background-color: #fff;
   border: none;
   border-radius: 45px;
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.2);
   transition: all 0.3s ease 0s;
   cursor: pointer;
   outline: none;

   display: block;
   margin: 0px auto;

   &:hover {
      color: #fff;
      transform: translateY(-7px);
   }
}

.floating {
   &:hover {
      transform: translateX(-50%) translateY(-7px);
   }
}

.green {
   &:hover {
      background-color: #2ee59d;
      box-shadow: 0px 15px 20px rgba(46, 229, 157, 0.4);
   }
}
.red {
   &:hover {
      background-color: #ff6251;
      box-shadow: 0px 15px 20px #ff5f4e7e;
   }
}
.yellow {
   &:hover {
      background-color: #ffd900;
      box-shadow: 0px 15px 20px #ffd90054;
   }
}
</style>
