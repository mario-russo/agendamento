# ============================================
# Stage 1: Build do Vue
# ============================================
# 1. Mudança para -slim para melhor compatibilidade e estabilidade de rede
FROM node:lts-slim AS build-stage
WORKDIR /app


# Ativa o Corepack para disponibilizar o Yarn automaticamente
RUN corepack enable

# 2. Copia arquivos de dependência
COPY package.json yarn.lock ./

# 3. Instala ignorando os scripts pós-instalação temporariamente
RUN yarn install --frozen-lockfile --ignore-scripts

# 4. Copia TODO o resto do projeto (agora o Quasar terá acesso ao quasar.config.js)
COPY . .

# 5. Executa manualmente o prepare (agora com arquivos) e faz o build
RUN npx quasar prepare
RUN yarn build

# ============================================
# Stage 2: Nginx servindo o Vue + proxy
# ============================================
FROM nginx:stable-alpine AS production-stage

# Copia a configuração customizada (serve Vue + proxy /api)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia os arquivos estáticos gerados no Stage 1
COPY --from=build-stage /app/dist/spa /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]