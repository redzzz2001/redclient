<p align="center">
  <img src="src/main/resources/assets/modid/icon.png" width="128" alt="Red Client">
</p>

<h1 align="center">Red Client 2.6</h1>

<p align="center">
  Utility client configuravel para Minecraft Java com Fabric.
  <br>
  <img alt="Minecraft" src="https://img.shields.io/badge/Minecraft-26.1--26.3-62B47A?style=flat-square">
  <img alt="Fabric" src="https://img.shields.io/badge/Fabric-0.19.5-DBD0B4?style=flat-square">
  <img alt="Java" src="https://img.shields.io/badge/Java-25-E76F00?style=flat-square">
  <img alt="Version" src="https://img.shields.io/badge/Red_Client-2.6-BD1E2D?style=flat-square">
</p>

---

Feito para quem quer configurar combate, movimento, ESP e automacoes em uma interface unica, com menu por categorias, rolagem e hotkeys individuais.

> [!WARNING]
> O uso de mods que alteram combate, movimento ou interacao pode violar as regras de servidores e resultar em kick, reset, suspensao ou banimento. O **redclient, seu autor e seus colaboradores nao se responsabilizam por banimentos, perda de progresso, danos ou qualquer outra consequencia causada pelo uso do mod**. Use por sua conta e risco e respeite as regras do servidor.

## Versoes suportadas

| Minecraft | Arquivo | Fabric Loader | Fabric API | Java |
|---|---|---:|---:|---:|
| 26.1 | `redclient 2.6 26.1.jar` | 0.19.5 | 0.145.1+26.1 | 25 |
| 26.1.1 | `redclient 2.6 26.1.1.jar` | 0.19.5 | 0.145.4+26.1.1 | 25 |
| 26.1.2 | `redclient 2.6 26.1.2.jar` | 0.19.5 | 0.155.3+26.1.2 | 25 |
| 26.2 | `redclient 2.6 26.2.jar` | 0.19.5 | 0.161.0+26.2 | 25 |
| 26.3 | `redclient 2.6 26.3.jar` | 0.19.5 | 0.163.0+26.3 | 25 |

Cada JAR aceita **somente** a versao de Minecraft indicada no nome. Nao coloque duas versoes do Red Client na pasta `mods` ao mesmo tempo.

## Recursos

### Combate

- Aim Bot com forca e alcance configuraveis
- Kill Aura com alcance configuravel
- Trigger Bot com carga minima configuravel
- Criticals
- Reach configuravel de 3 a 10 blocos
- Friend Protect e lista de amigos
- Mira PvP compacta

### Render

- Night Vision
- Player ESP com nome e vida
- Mob ESP com nome e vida
- Chest ESP
- Bed ESP
- Distancia dos ESPs configuravel

### Movimento

- Fly com velocidade configuravel
- Speed com multiplicador configuravel
- Super Jump com potencia configuravel
- Jesus
- No Fall, inclusive ao desativar o Fly

### Utilidades

- Auto Totem
- Auto Loot com atraso configuravel
- Fast Mine configuravel ate `1.40x`
- Hotkey individual para cada modulo
- Menu organizado em colunas com rolagem

## Instalacao

1. Instale o **Fabric Loader 0.19.5** para a sua versao do Minecraft.
2. Instale o **Fabric API** exato mostrado na tabela acima.
3. Copie o JAR correspondente e o Fabric API para a pasta `.minecraft/mods`.
4. Remova qualquer JAR antigo do Red Client da pasta `mods`.
5. Inicie o jogo pelo perfil Fabric.

## Controles

- `Alt direito`: abre e fecha o menu.
- `Botao do meio do mouse`: adiciona ou remove da lista o player que estiver na mira.
- `Opcoes > Controles > Red Client`: configura a hotkey de cada modulo.

As configuracoes numericas ficam no proprio menu do client.

## Limites importantes

- O servidor continua sendo autoritativo. Reach alto, Fly, Speed, Fast Mine e outros modulos podem ser recusados, corrigidos ou detectados pelo servidor.
- O limite de `1.40x` do Fast Mine evita mandar a conclusao da quebra antes do minimo normalmente aceito pelo servidor. Forcar mais que isso costuma causar o famoso bloco fantasma: quebra na tela e volta logo depois.
- Nenhum recurso e anunciado como indetectavel ou como bypass de anticheat.
- Use preferencialmente em mundos proprios, servidores de teste ou locais onde esses recursos sejam permitidos.

## Licenca

Distribuido sob a licenca [CC0 1.0](LICENSE).
