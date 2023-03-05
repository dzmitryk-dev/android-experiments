import subprocess
import common
import os

changed_files = subprocess.getoutput("git diff --name-only HEAD HEAD~1")
print("Files changed in commit: \n" + changed_files)

projects_dirs = common.find_all_projects_dirs()

for d in projects_dirs:
    for f in changed_files:
        print([d, f])
        print(os.path.commonpath([d,f]))

affected_projects = []
print("Affected projects:" + str(affected_projects))
