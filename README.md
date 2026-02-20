<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="120" alt="Logo de RDScore"/>
</p>

<h1 align="center">⚽ RDScore</h1>

<p align="center">
  <b>Predicciones de Fútbol y Análisis Estadístico</b><br/>
  <i>Impulsado por Modelos de Machine Learning</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Plataforma-Android-3DDC84?logo=android&logoColor=white" alt="Plataforma"/>
  <img src="https://img.shields.io/badge/Lenguaje-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Lenguaje"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="UI"/>
  <img src="https://img.shields.io/badge/Min%20SDK-29-brightgreen" alt="SDK Mínimo"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue" alt="SDK Objetivo"/>
</p>

---

## 📖 Acerca de

**RDScore** es una aplicación nativa de Android que proporciona **predicciones de fútbol basadas en IA** y un análisis estadístico profundo. Se conecta a un backend propio que ejecuta modelos de machine learning para generar pronósticos de partidos en las principales ligas europeas.

> ⚠️ **Aviso:** RDScore es una herramienta de análisis estadístico. No permite apuestas y no está afiliada a ninguna casa de apuestas. Todas las predicciones se basan en modelos matemáticos y datos históricos; no garantizan resultados.

---

## ✨ Características

### 🔮 Predicciones de Partidos
- **Tres modelos de predicción**: Conservador, Moderado y Agresivo — cada uno con diferentes perfiles de riesgo.
- **Tres mercados principales**: 
  - 🏆 **Resultado (1X2)** — Probabilidades de Victoria/Empate/Derrota.
  - ⚽ **Ambos Equipos Marcan (BTTS)** — Análisis de Sí/No.
  - 📊 **Más/Menos 2.5 Goles** — Probabilidad estadística.
- **Cálculo de goles esperados** (xG) para cada equipo.
- **Sistema de recomendación**: Señales de Apostar / No apostar según el modelo.

### 🔥 Cuotas Calientes (Hot Odds)
- Identifica **oportunidades de alto valor estadístico** en todos los partidos.
- Destaca selecciones donde el modelo encuentra una ventaja (edge) significativa.

### 📈 Estadísticas del Modelo y Resumen
- Seguimiento de la **precisión y el ROI** del modelo a lo largo del tiempo.
- Desgloses de precisión por mercado y por modelo.
- Resúmenes de rendimiento: total de partidos analizados y tasas de acierto.

### 🏟️ Detalles de Equipos y Ligas
- Estadísticas completas de equipos: récord (G-E-P), goles a favor/en contra, forma y posición.
- Comparativa de rendimiento local vs. visitante.
- Clasificaciones de liga con vista de tabla completa.
- Historial de partidos recientes por equipo.

### 👤 Sistema de Usuarios
- Registro, inicio de sesión y gestión de perfil.
- Recuperación de contraseña por correo electrónico.
- Edición de perfil y eliminación de cuenta.

### 🌍 Soporte Multi-idioma
Disponible en **5 idiomas**:

| 🇪🇸 Español | 🇬🇧 Inglés | 🇫🇷 Francés | 🇩🇪 Alemán | 🇮🇹 Italiano |
|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ |

### 🎨 Personalización
- Soporte para modo claro y oscuro.
- Cambio dinámico de tema desde los ajustes de la app.

---

## 🏗️ Stack Tecnológico

| Capa | Tecnología |
|---|---|
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Arquitectura** | MVVM + Clean Architecture |
| **Inyección (DI)** | Hilt (Dagger) |
| **Red** | Retrofit + OkHttp |
| **Almacenamiento** | Room Database + DataStore |
| **Asincronía** | Kotlin Coroutines + Flow |
| **Navegación** | Jetpack Navigation Compose |
| **Carga de Imágenes**| Coil |

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/rigobertods/rdscore/
├── core/
│   ├── common/         # Gestores de idioma, tema, UiState
│   ├── data/           # Room DB, DAOs, Entidades, Mappers
│   ├── network/        # Retrofit ApiService, Interceptores de Auth
│   ├── ui/             # Tema, Colores, Tipografía
│   └── util/           # Ayudantes de localización (Locale)
├── data/               # Gestión de sesión, manejo de errores
├── di/                 # Módulos de Hilt (App, DB, Network, Repository)
├── features/
│   ├── auth/           # Login, Registro, Recuperación
│   ├── cuotascalientes/# Funcionalidad de Hot Odds
│   ├── equipo/         # Detalles de equipo
│   ├── ligas/          # Datos de ligas
│   ├── partidos/       # Partidos, predicciones, detalles
│   ├── perfil/         # Gestión de perfil de usuario
│   └── resumen/        # Precisión del modelo y estadísticas
└── ui/
    ├── components/     # Componentes de Compose reutilizables
    ├── navigation/     # Rutas de navegación
    └── util/           # Utilidades de fecha, mapeo de mensajes
```

---

## 🚀 Comenzando

### Requisitos Previos
- **Android Studio** Ladybug o superior
- **JDK 11+**
- **Android SDK 36**

### Construcción y Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/RigobertoDS/RDScore.git

# Abrir en Android Studio y sincronizar Gradle
# Ejecutar en un emulador o dispositivo físico (API 29+)
```

---

## 🔗 Backend

RDScore se conecta a una API REST propia alojada en [rdscore.com](https://www.rdscore.com) que proporciona:
- Datos de partidos y predicciones de los modelos de ML.
- Autenticación de usuarios (basada en JWT con refresco de token).
- Datos estadísticos de equipos y ligas.
- Cálculos de Cuotas Calientes.

---

## 📄 Licencia

Este proyecto es de código abierto con fines educativos y de portafolio.  
Todos los derechos reservados © RigobertoDS.

---

<p align="center">
  <b>Creado con ❤️ por <a href="https://github.com/RigobertoDS">RigobertoDS</a></b>
</p>
