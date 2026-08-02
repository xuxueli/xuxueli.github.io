
## 组件

defineProps
emit
defineExpose
defineModel



defineProps 用法：
```
defineProps用法：“父传子”通信工具：
  - 子组件声明组件接收的 props（属性）
  - 父组件向子组件传递数据

  <pre>
    父组件：
      <SidebarLogo v-if="showLogo" :collapse="isCollapse" />
      ...
      const isCollapse = computed(() => !appStore.sidebar.opened)
    子组件：SidebarLogo.vue
      defineProps({   collapse: { type: Boolean, required: true }   })
      ...
      <template>
        <div :class="{ 'collapse': collapse }"></div>
      <template>
  </pre>>
 ```

defineEmits：
 ```
通知父组件：通过 emits 触发 “scroll、updateArrows” 等自定义事件

defineEmits用法：“子传父”通信工具。
  - 子组件声明组件触发的事件，返回 emit 函数（方法）。
  - 子组件向父组件传递数据 / 通知。

  <pre>
    父组件：
    <Child @my-event="handleEvent" />
    ...
    const handleEvent = (msg) => {    console.log(msg)  }
    子组件：
    const emit = defineEmits(['my-event', 'submit'])
    ...
    const handleClick = () => {   emit('my-event', 'Hello Parent!')    }   // 触发 'my-event' 事件
  </pre>

```

defineExpose 用法：
```
defineExpose用法：‌“父传子”通信工具：
   - 子组件向父组件暴露组件内部成员（属性 / 方法）
   - 父组件直接访问子组件内部状态

 <pre>
     父组件：
     <ScrollPane ref="scrollPaneRef" />
     ...
     const scrollPaneRef = ref(null)
     ...
     scrollPaneRef.value.abc()
     子组件：ScrollPane.vue
     defineExpose({ scrollToStart })
     ...
     function abc() {    ...  }
 </pre>
```

## 响应

ref
reactive
toRefs


响应式用法：
- ref：主要用于处理基本数据类型（如字符串、数字、布尔值）以及简单的对象和数组。ref 允许将一个普通的值变成响应式，当值改变时，所有依赖于它的组件或计算属性也会自动更新。
- reactive：主要用于将复杂的对象（如对象和数组）转换为响应式对象，从而使得对象内部的所有属性都具有响应性。这意味着，当对象的属性发生变化时，所有依赖于这些属性的组件或计算属性也会自动更新。
- toRef：将响应式对象中的‌每个属性‌单独提取出来，变成独立的 ref，以便在解构（destructuring）后依然保持响应性。
  使用方式：
- 方式1：直接使用 ref（更常见）
- 方式2：使用 reactive + toRefs（适用于多个属性的对象，解构后仍保持响应性）