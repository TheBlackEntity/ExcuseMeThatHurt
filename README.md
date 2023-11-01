# ExcuseMeThatHurt
ExcuseMeThatHurt is a plugin for 1.20.1 that prevents players from cheesing neutral mobs.

If you place lava, fire or push a neutral mob a cliff, it will get angry at you.

Iron Golems will get angry at you if you do this to villagers.

You will also get the Bad Omen effect by killing a Pillager Captain with a cheesy method.


This plugin was inspired by this mod:
https://github.com/Khajiitos/ISWYDT

## Installation

1. Download or compile the JAR
2. Drop the JAR in your server's plugin folder
3. Restart your server

## Configuration

<details>
<summary>Configuration</summary>

```yaml
#What range should we tell mobs about hazardous materials in?
#Recommended: 6-8
WarnMobsRange: 8
#What range should we allow mobs to target a player on hurt?
MaxAttackDistance: 15
Threats:
  Push:
    #Do you want mobs to realise they're being pushed of a cliff?
    #Disable, if you encounter lags
    Enabled: true
    #When do you want a mob to forget this action?
    #The unit is seconds
    #Recommended: 3-5
    Forget: 5
  Lava:
    #Do you want mobs to realise you place lava on them?
    Enabled: true
    #When do you want a mob to forget this action?
    #The unit is seconds
    #Recommended: 10-15
    Forget: 10
  Fire:
    #Do you want mobs to realise you set them on fire?
    Enabled: true
    #When do you want a mob to forget this action?
    #The unit is seconds
    #Recommended: 10-15
    Forget: 10
SpecialCases:
  Villagers:
    #Do you want villagers to snitch on you?
    #Disable, if you encounter lags
    AlertGolems: true
    #What range should we notify golems in?
    Range: 32
  ZombifiedPiglin:
    #Do you want the Pigmen to unite?
    #Disable, if you encounter lags
    AlertPigmen: true
    #What range should we notify other pigmen in?
    Range: 32
  Pillager:
    #Do you want players to receive the Bad Omen effect?
    BadOmen: true
```

</details>

## Support
Discord: [https://discord.gg/dBhfCzdZxq](https://discord.gg/GxEFhVY6ff)

Github: [Issues Page](https://github.com/TheBlackEntity/ExcuseMeThatHurt/issues)
