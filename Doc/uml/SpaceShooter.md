classDiagram
direction TB
class Alien {
  + Alien() 
  + Alien(int, int, int) 
  - animation() void
  - lcm(int, int) float
  + isHit() void
  - shoot() void
  - getFrame(int) GreenfootImage
  - slideIn() void
  + spawnUpgrade() void
  + act() void
   boolean dead
}
class AlienDeath {
  + AlienDeath(int) 
  + act() void
}
class Ammunition {
  + Ammunition() 
  - animate() void
  + act() void
}
class Bullet {
  + Bullet(int) 
  + Bullet(int, boolean) 
  - explode() void
  - init() void
  - checkSurroundings() void
  + startExplosion() void
  + act() void
  - moveOneStep() void
  - animation() void
}
class BulletData {
  + BulletData(int, int, int, int, int, int, int, int, int, int, int) 
  + getBullet(int) BulletData
}
class Button {
  + Button(String, Color, Color, OnClick) 
  - init() void
  + act() void
}
class DbService {
  + DbService() 
  + close() void
  + connect() void
  + addScore(String, int) boolean
  + updateSettings(Settings) boolean
  + toggleMute() void
   Settings settings
   ArrayList~Score~ scores
}
class Fog {
  + Fog(int) 
  + moveOnce() void
  + act() void
  - animation() void
}
class GameState {
<<enumeration>>
  + GameState() 
  + valueOf(String) GameState
  + values() GameState[]
}
class LeaderBoardComponent {
  + LeaderBoardComponent(World) 
  + LeaderBoardComponent(int, int) 
  + refresh() void
  - init(int, int) void
}
class Menu {
  + Menu() 
  + Menu(GameState) 
  - init() void
  + refresh() void
  + act() void
  - clearAll() void
  - generateGameOver() void
  - addScore() void
  - generateGameStart() void
  - onClick() void
}
class OnClick {
<<Interface>>
  + onClick() void
}
class Score {
  + Score(String, int, int) 
}
class Settings {
  + Settings() 
}
class SoundService {
  + SoundService() 
  + SoundService(String) 
  ~ GreenfootSound sound
  + playSound(String) void
  + playSound() void
  + setSound() void
  + stopSound() void
  + setVolume() void
   String sound
   boolean playing
}
class Space {
  + Space() 
  + generateAmmunition() void
  - setNewCurrentBackground() void
  - decrementAnimationSeconds() void
  + addScore(int) void
  + gameOver() void
  - levelUp() void
  - toggleMute() void
  + refreshGameStats() void
  - alienFormation() void
  - decrementAnimationMilliseconds() void
  - generateAliens() void
  - playRandomBackgroundMusic() void
  - runAnimationTimer() void
  + startGame() void
  - generateUpgrade() void
  - alienFormation1(ArrayList~Alien~) void
  - alienFormation3(ArrayList~Alien~) void
  - generateBackground() void
  - decrementAnimationMinutes() void
  + act() void
  - refreshBackground() void
  - alienFormation2(ArrayList~Alien~) void
}
class SpaceShip {
  + SpaceShip() 
  - animation() void
  - playDeathAnimation() void
  - coolDown() void
  + act() void
  + controls() void
  - init() void
  - resetBulletType() void
  + isHitByUpgrade() void
  + isHit() void
}
class Star {
  + Star(int) 
  + Star() 
  - animation() void
  + act() void
}
class UI {
  + UI() 
  - getHealthImage(int) GreenfootImage
  + refreshUI() void
  - getAmmunitionImage(int) GreenfootImage
  + act() void
  - getShieldImage(int) GreenfootImage
}
class Upgrade {
  + Upgrade(UpgradeType) 
  + Upgrade() 
  + act() void
  - animate() void
}
class UpgradeIcon {
  + UpgradeIcon(UpgradeType) 
  + nextFrame() void
}
class UpgradeType {
<<enumeration>>
  + UpgradeType() 
  + values() UpgradeType[]
  + valueOf(String) UpgradeType
}

Alien  ..>  AlienDeath : «create»
Alien  ..>  Bullet : «create»
Alien  ..>  Upgrade : «create»
AlienDeath  ..>  SoundService : «create»
Bullet "1" *--> "bulletSound 1" SoundService 
Bullet  ..>  SoundService : «create»
BulletData "1" *--> "bullets *" BulletData 
BulletData  ..>  BulletData : «create»
Button "1" *--> "onClick 1" OnClick 
DbService  ..>  Score : «create»
DbService  ..>  Settings : «create»
Fog  ..>  Fog : «create»
Menu  ..>  Button : «create»
Menu "1" *--> "gameState 1" GameState 
Menu "1" *--> "leaderboard 1" LeaderBoardComponent 
Menu  ..>  LeaderBoardComponent : «create»
Menu  ..>  Space : «create»
Button  -->  OnClick 
Space  ..>  Alien : «create»
Space  ..>  Ammunition : «create»
Space "1" *--> "toggleMute 1" Button 
Space  ..>  Button : «create»
Space  ..>  Fog : «create»
Space  ..>  Menu : «create»
Space "1" *--> "backgroundMusic 1" SoundService 
Space  ..>  SoundService : «create»
Space  ..>  SpaceShip : «create»
Space  ..>  Star : «create»
Space  ..>  UI : «create»
Space  ..>  Upgrade : «create»
SpaceShip  ..>  AlienDeath : «create»
SpaceShip  ..>  Bullet : «create»
SpaceShip "1" *--> "heartBreakSound 1" SoundService 
SpaceShip  ..>  SoundService : «create»
SpaceShip "1" *--> "activeUpgrades *" UpgradeType 
Star  ..>  Star : «create»
UI "1" *--> "upgradeIcons *" UpgradeIcon 
UI  ..>  UpgradeIcon : «create»
Upgrade "1" *--> "upgradeType 1" UpgradeType 
UpgradeIcon "1" *--> "upgradeType 1" UpgradeType 
