param([string]$JavaHome=$env:JAVA_HOME)
$ErrorActionPreference='Stop'
$root=Split-Path -Parent $PSScriptRoot
$stage=Join-Path $root ('build/visual-tests-'+[Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Force $stage | Out-Null
& "$JavaHome/bin/javac.exe" --release 8 -d $stage (Join-Path $root 'src/tv/casa/launcher/VisualTokens.java') (Join-Path $root 'src/tv/casa/launcher/DesignMetrics.java') (Join-Path $root 'tests/VisualContractTest.java')
if($LASTEXITCODE){throw 'Visual contract compilation failed.'}
& "$JavaHome/bin/java.exe" -cp $stage tv.casa.launcher.VisualContractTest
if($LASTEXITCODE){throw 'Visual contract failed.'}

[xml]$tokens=Get-Content (Join-Path $root 'res/values/visual_tokens.xml')
$javaTokens=Get-Content (Join-Path $root 'src/tv/casa/launcher/VisualTokens.java') -Raw
foreach($color in $tokens.resources.color){$name=$color.name.Replace('velira_','').ToUpperInvariant();$hex=([string]$color.'#text').TrimStart('#');if($javaTokens -notmatch ($name+'=0xFF'+$hex)){throw "XML/Java visual token mismatch: $name"}}
Write-Output 'Android XML and Java palette tokens agree.'
