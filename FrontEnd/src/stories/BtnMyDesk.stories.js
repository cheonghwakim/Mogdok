import BtnMyDesk from '../components/ui/BtnMyDesk.vue';

export default {
   title: 'Button/MyDesk',
   component: BtnMyDesk,
};

const Template = () => ({
   components: { BtnMyDesk },
   template: '<btn-my-desk @onClick="onClick" />',
});

export const Default = Template.bind({});
