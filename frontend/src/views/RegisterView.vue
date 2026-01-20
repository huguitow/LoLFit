<script setup>
    import { ref } from 'vue'
    const email = ref('')
    const password = ref('')
    const username = ref('')
    const handleRegister = async () => {
        try {
            const reponse = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email.value,
                    password: password.value,
                    username: username.value
                })
            }) 

            const data = await reponse.json()
            console.log('Réponse du serveur :', data)
            
        } catch (error) {
            console.error('Erreur lors de l\'inscription :', error)
        }
    }
</script>

<template>
    <h1>Page d'inscription</h1>
    <input type="email" v-model="email" />
    <input type="password" v-model="password" />
    <input type="text" v-model="username" />
    <button @click="handleRegister">S'inscrire</button>
</template>

<style scoped>
</style>
