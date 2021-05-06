import { action } from '@storybook/addon-actions';
import BtnCommand from '../components/ui/BtnCommand.vue';

export default {
   title: 'Button/Command',
   component: BtnCommand,
};

const Template = (args, { argTypes }) => ({
   props: Object.keys(argTypes),
   components: { BtnCommand },
   template: '<btn-command :label="label" @click="action"/>',
   methods: {
      action: action('button-clicked'),
   },
});

export const Study = Template.bind({});
Study.args = {
   label: '공부 하자!',
};

export const Desk = Template.bind({});
Desk.args = {
   label: '책상 구경하기',
};
