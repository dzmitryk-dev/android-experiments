import subprocess
import common
import os

changed_files = subprocess.getoutput("git diff --name-only HEAD HEAD~1").split('\n')
print("Files changed in commit:\n" + str(changed_files))

projects_dirs = common.find_all_projects_dirs()

affected_projects = [d for d in projects_dirs if next(filter(lambda f: os.path.commonpath([d, f]), changed_files), None)]
print("Affected projects:\n" + str(affected_projects))

for p in affected_projects:
    common.build_project(p)
