<script setup>
    import { ref } from 'vue'
    const email = ref('')
    const password = ref('')
    const handleLogin = async () => {
        try {
            const reponse = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email.value,
                    password: password.value
                })
            }) 

            const data = await reponse.json()
            console.log('Réponse du serveur :', data)
            
        } catch (error) {
            console.error('Erreur lors de la connexion :', error)
        }
    }
</script>

<template>
    <h1>Page de connexion</h1>
    <input type="email" v-model="email" />
    <input type="password" v-model="password" />
    <button @click="handleLogin">Se connecter</button>
</template>

<style scoped>
</style>
