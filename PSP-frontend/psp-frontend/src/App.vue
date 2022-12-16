<template>
  <div id="app">
    <NavbarComponent></NavbarComponent>
    <router-view></router-view>
  </div>
</template>

<script>
import NavbarComponent from './components/NavbarComponent.vue';


export default {
  name: 'App',
  data() {
    return {
      user: null
    }
  },
  components: { NavbarComponent },
}
</script>
