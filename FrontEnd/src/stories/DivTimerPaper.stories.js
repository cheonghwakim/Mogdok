import DivTimerPaper from '../components/ui/DivTimerPaper.vue';

export default {
   title: 'Display/TimerPaper',
   component: DivTimerPaper,
   argTypes: {
      type: { control: { type: 'select', options: ['study', 'rest'] } },
   },
};

const Template = (args, { argTypes }) => ({
   props: Object.keys(argTypes),
   components: { DivTimerPaper },
   template: '<div-timer-paper :type="type" :timer="timer" />',
});

export const Study = Template.bind({});
Study.args = {
   type: 'study',
   timer: '12:31:44',
};

export const Rest = Template.bind({});
Rest.args = {
   type: 'rest',
   timer: '00:31:44',
};
