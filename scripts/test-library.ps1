param([string]$JavaHome=$env:JAVA_HOME)
$ErrorActionPreference='Stop'
$root=Split-Path -Parent $PSScriptRoot
$stage=Join-Path $root ('build/library-tests-'+[Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Force $stage | Out-Null
& "$JavaHome/bin/javac.exe" --release 8 -d $stage (Join-Path $root 'src/tv/casa/launcher/LibraryQuery.java') (Join-Path $root 'tests/LibraryQueryTest.java')
if($LASTEXITCODE){throw 'Library test compilation failed.'}
& "$JavaHome/bin/java.exe" -cp $stage tv.casa.launcher.LibraryQueryTest
if($LASTEXITCODE){throw 'Library query tests failed.'}
