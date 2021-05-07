import BtnLeaveDesk from '../components/ui/BtnLeaveDesk.vue';

export default {
   title: 'Button/LeaveDesk',
   component: BtnLeaveDesk,
};

const Template = () => ({
   components: { BtnLeaveDesk },
   template: '<btn-leave-desk @onClick="onClick" />',
});

export const Default = Template.bind({});
