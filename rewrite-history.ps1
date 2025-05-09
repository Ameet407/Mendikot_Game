$OldEmail = "harshverma4466@gmail.com"
$CorrectName = "ameet407"
$CorrectEmail = "ameetkumar119@gmail.com"

git filter-branch -f --env-filter "
if [ `$GIT_COMMITTER_EMAIL = '$OldEmail' ]
then
    export GIT_COMMITTER_NAME='$CorrectName'
    export GIT_COMMITTER_EMAIL='$CorrectEmail'
fi
if [ `$GIT_AUTHOR_EMAIL = '$OldEmail' ]
then
    export GIT_AUTHOR_NAME='$CorrectName'
    export GIT_AUTHOR_EMAIL='$CorrectEmail'
fi
" --tag-name-filter cat -- --branches --tags 