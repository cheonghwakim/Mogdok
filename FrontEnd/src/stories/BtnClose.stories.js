import BtnClose from '../components/ui/BtnClose.vue';

export default {
   title: 'Button/Close',
   component: BtnClose,
};

const Template = () => ({
   components: { BtnClose },
   template: '<btn-close @onClick="onClick" />',
});

export const Default = Template.bind({});
