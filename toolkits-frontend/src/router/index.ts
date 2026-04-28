import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/projects',
    },
    {
      path: '/projects',
      name: 'ProjectList',
      component: () => import('@/views/project/ProjectList.vue'),
    },
    {
      path: '/projects/:id',
      name: 'ProjectDetail',
      component: () => import('@/views/project/ProjectDetail.vue'),
    },
    {
      path: '/personnel',
      name: 'PersonnelList',
      component: () => import('@/views/personnel/PersonnelList.vue'),
    },
    {
      path: '/roles',
      name: 'RoleManagement',
      component: () => import('@/views/personnel/RoleManagement.vue'),
    },
  ],
})

export default router
